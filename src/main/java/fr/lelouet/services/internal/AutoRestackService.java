package fr.lelouet.services.internal;

import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.staking.bean.PersonalLeftQuota;
import fr.lelouet.services.external.binance.staking.bean.ProductResponse;
import fr.lelouet.services.external.binance.staking.bean.StakingProducts;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.bean.SlackMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    /**
     * Fonction global de restack automatique
     * Les cryptos dispos dans stack doivent etre directement stackés lorsqu'elles sont disponible
     */
    public void automatiqueReStack() {
        // Récupération des cryptos depuis le compte spot de l'utilisateur
        CoinsWalletInformations coinsWalletInformations = binanceApi.getCoinsInformationsOfSpotWallet();

        // Coins dispos pour le stacking
        coinsWalletInformations.getCoinsAvalaible()
            .stream()
            // Filtre les cryptos volontairement à ignorer
            .filter(coinWallet -> configurationService.ignoreAutoStakingCryptoList().contains(coinWallet.coin().name()))
            // Pour chaque crypto disponible
            .forEach(coinWallet -> {
                String logFind = AutoRestackService.concatFind(coinWallet.coin().name(), coinWallet.free());
                logger.info(logFind);
                slackService.sendMessage(logFind, SlackMessageType.AUTO_STAKING);
                // Récupération des stakings products disponible pour cette crypto
                StakingProducts stakingProducts = binanceApi.getStakingProducts(coinWallet.coin());
                // Pour chaque produits disponibles,
                stakingProducts.stakingList()
                    .stream()
                    // Filtre les stakings dont le minimum est trop élevé par rapport aux coins de l'utilisateur
                    .filter(projectStaking -> Double.valueOf(projectStaking.quota().minimum()) >= Double.valueOf(coinWallet.free()))
                    // Filtre les stakings déjà remplis totalement
                    .filter(projectStaking -> {
                        // Récupération du quota réstant sur le produit
                        PersonalLeftQuota personalLeftQuota = binanceApi.getPersonalLeftQuota(projectStaking.projectId());
                        Double leftQuota = Double.valueOf(personalLeftQuota.leftPersonalQuota());
                        Double totalPersonnalQuota = Double.valueOf(projectStaking.quota().totalPersonalQuota());
                        // Comparaison avec le QuotaTotal de l'utilisateur
                        return (leftQuota > 0) && leftQuota > totalPersonnalQuota;
                    })
                    .forEach(projectStaking -> {
                        // Tentative de staking du produit avec le montant de la crypto disponible
                        ProductResponse productResponse = binanceApi.postStakingProducts(projectStaking.projectId(), Double.valueOf(coinWallet.free()));
                        String log = AutoRestackService.concatEndResult(
                            coinWallet.coin().name(),
                            coinWallet.free(),
                            projectStaking.projectId(),
                            productResponse.positionId(),
                            productResponse.success()
                        );
                        // Envoie de la notif slack
                        slackService.sendMessage(log, SlackMessageType.AUTO_STAKING);
                        logger.info(log);
                    });
            });
    }

    private static String concatEndResult(
        String coinName,
        String amount,
        String projectId,
        String positionId,
        boolean success
    ) {
        return "[" + coinName + "] d'un montant [" + amount + "] pour le produit [" + projectId + "] avec une position [" + positionId + "] : [" + success + "]";
    }

    private static String concatFind(String coinName, String amount) {
        return "Stacking à identifier pour [" + coinName + "] d'un montant de [" + amount + "]";
    }

    public void destackFlexibleStaking() {
        // Todo
        // Regarder ls cryptos en stacking flexible
        // Regarder s'il y a un stacking non flex de cette crypto => dispo + montant minimum
        // Rappatrié le flex dans spot et déclancher l'automatiqueRestack
    }
}
