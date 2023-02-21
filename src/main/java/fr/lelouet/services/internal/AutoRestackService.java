package fr.lelouet.services.internal;

import com.coreoz.plume.jersey.errors.WsException;
import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.errors.ProjectError;
import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.staking.bean.PersonalLeftQuota;
import fr.lelouet.services.external.binance.staking.bean.ProductResponse;
import fr.lelouet.services.external.binance.staking.bean.ProjectStaking;
import fr.lelouet.services.external.binance.staking.bean.StakingProducts;
import fr.lelouet.services.external.binance.wallet.bean.CoinWallet;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.bean.SlackMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Algorithmes de restaking automatique
 */
@Singleton
public class AutoRestackService {
    private static final Logger logger = LoggerFactory.getLogger(AutoRestackService.class);

    private final BinanceApi binanceApi;
    private final ConfigurationService configurationService;
    private final SlackService slackService;

    @Inject
    public AutoRestackService(
        BinanceApi binanceApi,
        ConfigurationService configurationService,
        SlackService slackService
    ) {
        this.binanceApi = binanceApi;
        this.configurationService = configurationService;
        this.slackService = slackService;
    }

    public void destackFlexibleStaking() {
        // Todo
        // Regarder ls cryptos en stacking flexible
        // Regarder s'il y a un stacking non flex de cette crypto => dispo + montant minimum
        // Rappatrié le flex dans spot et déclancher l'automatiqueRestack
    }

    /**
     * Fonction global de restack automatique
     * Les cryptos dispos dans stack doivent etre directement stackés lorsqu'elles sont disponible
     * todo a rediviser en sous fonctions
     */
    public void automaticReStack() {
        // Récupération des cryptos disponibles depuis le compte spot de l'utilisateur
        List<CoinWallet> coinsAvalaible = this.getSpotWalletCoins().getCoinsAvalaible();
        for (CoinWallet coinWallet : coinsAvalaible) {
            // Filtre les cryptos volontairement à ignorer
            if (configurationService.ignoreAutoStakingCryptoList().contains(coinWallet.coin())) {
                logger.debug("[AUTO_STAKING] [{}] ignoré par la configuration projet", coinWallet.coin());
                break;
            }
            try {
                // log et slack le début de la recherche
                informOfSearch(coinWallet.coin(), coinWallet.free());
                // Récupération des stakings products disponible pour cette crypto
                StakingProducts stakingProducts = binanceApi.getStakingProducts(coinWallet.coin());
                // Pour chaque produits disponibles,
                for (ProjectStaking projectStaking : stakingProducts.stakingList()) {
                    // Filtre les stakings dont le minimum est trop élevé par rapport aux coins de l'utilisateur
                    if (Double.valueOf(coinWallet.free()).compareTo(Double.valueOf(projectStaking.quota().minimum())) < 0) {
                        logger.debug("[AUTO_STAKING] [{}] => [{}] ignoré car le montant du wallet est plus faible que le quota minimum", coinWallet.coin(), projectStaking.projectId());
                        break;
                    }
                    // Récupération du quota réstant sur le produit
                    PersonalLeftQuota personalLeftQuota = binanceApi.getPersonalLeftQuota(projectStaking.projectId());
                    Double leftQuota = Double.valueOf(personalLeftQuota.leftPersonalQuota());
                    Double totalPersonnalQuota = Double.valueOf(projectStaking.quota().totalPersonalQuota());
                    // Comparaison avec le QuotaTotal de l'utilisateur
                    // Filtre les stakings déjà remplis totalement
                    if ((leftQuota <= 0) && leftQuota.compareTo(totalPersonnalQuota) < 0) {
                        logger.debug("[AUTO_STAKING] [{}] => [{}] ignoré car le quota restant est trop bas", coinWallet.coin(), projectStaking.projectId());
                        break;
                    }

                    Double freeCoin = Double.valueOf(coinWallet.free());
                    int retval = Double.compare(leftQuota, freeCoin);

                    // Tentative de staking du produit avec le montant de la crypto disponible ou du quota restant
                    if (retval > 0) {
                        this.postStakingProduct(projectStaking.projectId(), freeCoin, coinWallet.coin());
                    } else {
                        this.postStakingProduct(projectStaking.projectId(), leftQuota, coinWallet.coin());
                    }
                }
            } catch (Exception e) {
                logger.error("[AUTO_STAKING] Erreur durant l'autoStaking de la crypto [{}]", coinWallet.coin(), e);
            }
        }

    }

    /**
     * Récupére les cryptos disponibles sur le compte SPOT de l'utilisateur
     */
    public CoinsWalletInformations getSpotWalletCoins() {
        CoinsWalletInformations coinsWalletInformations = binanceApi.getCoinsInformationsOfSpotWallet();
        if (coinsWalletInformations != null) {
            return coinsWalletInformations;
        }
        throw new WsException(ProjectError.NO_COINS_ON_SPOT_WALLET);
    }


    /**
     * Post un staking dans Binance
     *
     * @param projectStakingId : Id technique du produit de staking
     * @param amount           : Montant de la crypto à "dépenser"
     * @param asset            : Trigramme de la crypto
     */
    private void postStakingProduct(String projectStakingId, Double amount, String asset) {
        ProductResponse productResponse = binanceApi.postStakingProducts(projectStakingId, amount);
        String log = AutoRestackService.concatEndResult(
            asset,
            String.valueOf(amount),
            projectStakingId,
            productResponse.positionId(),
            productResponse.success()
        );
        slackService.sendMessage(log, SlackMessageType.AUTO_STAKING);
        logger.info(log);
    }

    /**
     * Informe l'utilisateur sur slack et log du début de la recherche
     *
     * @param asset  : Trigramme de la crypto
     * @param amount : Montant total
     */
    private void informOfSearch(String asset, String amount) {
        String logFind = AutoRestackService.concatFind(asset, amount);
        logger.info(logFind);
        slackService.sendMessage(logFind, SlackMessageType.AUTO_STAKING);
    }

    private static String concatEndResult(
        String coinName,
        String amount,
        String projectId,
        String positionId,
        boolean success
    ) {
        return "[AUTO_STAKING] [" + coinName + "] stacké pour un montant de [" + amount + "] sur le produit [" + projectId + "] avec une position [" + positionId + "] : [" + success + "]";
    }

    private static String concatFind(String coinName, String amount) {
        return "[AUTO_STAKING] Stacking à rechercher pour [" + coinName + "] d'un montant total de [" + amount + "]";
    }
}
