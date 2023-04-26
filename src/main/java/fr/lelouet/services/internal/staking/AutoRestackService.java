package fr.lelouet.services.internal.staking;

import com.coreoz.plume.jersey.errors.WsException;
import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.errors.ProjectError;
import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.saving.bean.FlexiblePosition;
import fr.lelouet.services.external.binance.saving.enums.RedeemType;
import fr.lelouet.services.external.binance.staking.bean.ProductResponse;
import fr.lelouet.services.external.binance.staking.bean.ProjectStaking;
import fr.lelouet.services.external.binance.staking.bean.Quota;
import fr.lelouet.services.external.binance.staking.bean.StakingProducts;
import fr.lelouet.services.external.binance.wallet.bean.CoinWallet;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.enums.SlackMessageType;
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
    private final SlackService slackService;
    private final List<String> ignoredCryproStaked;
    private final List<String> ignoredCryproRedeem;

    @Inject
    public AutoRestackService(
        BinanceApi binanceApi,
        ConfigurationService configurationService,
        SlackService slackService
    ) {
        this.binanceApi = binanceApi;
        this.ignoredCryproStaked = configurationService.ignoreAutoStakingCryptoList();
        this.ignoredCryproRedeem = configurationService.ignoreRedeemFlexibleCryptoList();
        this.slackService = slackService;
    }

    // todo : ajouter des logs et des gestions de cas d'erreurs
    // Todo : implémenter un % minimum en conf sur les apy, pour décider de destack le flex en staked
    // todo : refacto pour séparer la logique
    public void redeemFlexibleStaking() {
        String logStart = "[REDEEM_FLEXIBLE] Début";
        logger.debug(logStart);
        slackService.sendMessage(logStart, SlackMessageType.AUTO_REDEEM);
        // Récupère l'ensemble des positions prises sur des produits flexibles.
        List<FlexiblePosition> flexiblePositions = binanceApi.flexibleProductPosition();
        for (FlexiblePosition flexiblePosition : flexiblePositions) {
            boolean redeemThisCrypto = false;
            int retval = 0;
            Double leftQuota = 0.0;
            // informatif
            String assetName = flexiblePosition.asset();
            Double freeAmount = Double.valueOf(flexiblePosition.totalAmount());
            String annualRate = flexiblePosition.annualInterestRate();
            // Filtre des cryptos ignorés volontairement dans la configuration projet
            if (ignoredCryproRedeem.contains(assetName)) {
                logger.debug("[REDEEM_FLEXIBLE] [{}] ignoré par la configuration projet", assetName);
                break;
            }
            try {
                logger.debug("[REDEEM_FLEXIBLE] [{}] flexible avec un montant dispo de [{}]", assetName, freeAmount);
                StakingProducts stakingProducts = binanceApi.getStakingProducts(assetName);
                for (ProjectStaking projectStaking : stakingProducts.orderByApy()) {
                    String projetId = projectStaking.projectId();
                    String projectApy = projectStaking.detail().apy();
                    // Filtre les stakings dont l'apy est plus basse que l'apy du flex
                    if (Double.valueOf(annualRate).compareTo(Double.valueOf(projectStaking.detail().apy())) >= 0) {
                        logger.debug("[REDEEM_FLEXIBLE] [{}] => Ignoré car l'APY du flexible actuel [{}] est plus élevé que celui du staking [{}] d'apy [{}]", assetName, annualRate, projetId, projectApy);
                        break;
                    }
                    leftQuota = this.validateStackProduct(projetId, assetName, freeAmount, projectStaking.quota());
                    if (leftQuota != null) {
                        logger.info("[REDEEM_FLEXIBLE] [{}] => Tentative de tranformation du flexible d'apy [{}] en staking [{}] d'apy [{}]", assetName, annualRate, projetId, projectApy);

                        retval = Double.compare(leftQuota, freeAmount);
                        redeemThisCrypto = true;
                        break;
                    }
                }
                if (redeemThisCrypto) {
                    // Tentative de staking du produit avec le montant de la crypto disponible ou du quota restant
                    if (retval > 0) {
                        binanceApi.redeemFlexibleProduct(flexiblePosition.productId(), Double.valueOf(flexiblePosition.freeAmount()), RedeemType.FAST);
                        slackService.sendMessage("[REDEEM_FLEXIBLE_SUCCESS] ProductId [" + flexiblePosition.productId() + "] redeem d'un montant de [" + Double.valueOf(flexiblePosition.freeAmount()) + "]", SlackMessageType.AUTO_REDEEM);
                    } else {
                        binanceApi.redeemFlexibleProduct(flexiblePosition.productId(), leftQuota, RedeemType.FAST);
                        slackService.sendMessage("[REDEEM_FLEXIBLE_SUCCESS] ProductId [" + flexiblePosition.productId() + "] redeem d'un montant de [" + leftQuota + "]", SlackMessageType.AUTO_REDEEM);
                    }
                }
            } catch (Exception e) {
                logger.error("[REDEEM_FLEXIBLE] Erreur de la crypto [{}]", assetName, e);
            }
        }
        String logEnd = "[REDEEM_FLEXIBLE] Fin";
        logger.debug(logEnd);
        slackService.sendMessage(logEnd, SlackMessageType.AUTO_REDEEM);
    }

    /**
     * Fonction global de restack automatique
     * Les cryptos dispos dans stack doivent etre directement stackés lorsqu'elles sont disponible
     */
    public void automaticReStack() {
        String logStart = "[AUTO_STAKING] debut";
        logger.debug(logStart);
        slackService.sendMessage(logStart, SlackMessageType.AUTO_STAKING);
        // Récupération des cryptos disponibles depuis le compte spot de l'utilisateur
        List<CoinWallet> coinsAvalaible = this.getSpotWalletCoins().getCoinsAvalaible();
        for (CoinWallet coinWallet : coinsAvalaible) {
            String assetName = coinWallet.coin();
            Double amountFree = Double.valueOf(coinWallet.free());
            // Filtre les cryptos volontairement à ignorer
            if (ignoredCryproStaked.contains(assetName)) {
                logger.debug("[AUTO_STAKING] [{}] ignoré par la configuration projet", assetName);
                break;
            }
            try {
                // log et slack le début de la recherche
                logger.info("[AUTO_STAKING] Stacking à rechercher pour [{}] d'un montant total de [{}]", assetName, amountFree);
                // Récupération des stakings products disponible pour cette crypto
                List<ProjectStaking> stakingProducts = binanceApi.getStakingProducts(assetName).orderByApy();
                // Pour chaque produits de stakings disponibles,
                for (ProjectStaking projectStaking : stakingProducts) {
                    this.tryStackAsset(projectStaking.projectId(), assetName, amountFree, projectStaking.quota());
                }
            } catch (Exception e) {
                logger.error("[AUTO_STAKING] Erreur de la crypto [{}]", assetName, e);
            }
            logger.debug("[AUTO_STAKING] Fin de la recherche de staking pour [{}]", assetName);
        }
        String logEnd = "[AUTO_STAKING] End";
        logger.debug(logEnd);
        slackService.sendMessage(logEnd, SlackMessageType.AUTO_STAKING);
    }

    /**
     * Permet d'essayer de staker un produit en particulier.
     *
     * @param productId : Produit que l'utilisateur veut stacker
     * @param assetName : Trigramme de la crypto
     * @param amount : Amount maximum qui sera stacké, au minimum => Le quota restant
     * @param productQuota : Le quota restant sur le produit
     */
    private void tryStackAsset(String productId, String assetName, Double amount, Quota productQuota) {
        Double leftQuota = this.validateStackProduct(productId, assetName, amount, productQuota);
        // Si leftQuota null c'est que le stacking ne respecte pas les conditions necessaires
        if (leftQuota != null) {
            int compareQuotaWithWallet = Double.compare(leftQuota, amount);
            // Tentative de staking du produit avec le montant de la crypto disponible ou du quota restant
            if (compareQuotaWithWallet > 0) {
                this.postStakingProduct(productId, amount, assetName);
            } else {
                this.postStakingProduct(productId, leftQuota, assetName);
            }
        }
    }

    /**
     * Fonction de validation des montants d'un produit
     * Est ce que le montant voulu n'est pas plus petit que le quota minimum ?
     * Est ce qu'il reste un montant disponible sur le produit ?
     */
    private Double validateStackProduct(String productId, String assetName, Double amount, Quota productQuota) {
        // Filtre les stakings dont le minimum est trop élevé par rapport aux coins de l'utilisateur
        if (amount.compareTo(Double.valueOf(productQuota.minimum())) < 0) {
            logger.debug("[{}] => [{}] ignoré car le montant du wallet est plus faible que le quota minimum", assetName, productId);
            return null;
        }
        // Récupération du quota restant sur le produit
        Double leftQuota = binanceApi.getPersonalLeftQuota(productId);
        Double totalPersonnalQuota = Double.valueOf(productQuota.totalPersonalQuota());
        // Comparaison avec le QuotaTotal de l'utilisateur
        // Filtre les stakings déjà remplis totalement
        if ((leftQuota <= 0) && leftQuota.compareTo(totalPersonnalQuota) < 0) {
            logger.debug("[{}] => [{}] ignoré car le quota restant est trop bas", assetName, productId);
            return null;
        }
        return leftQuota;
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
        if (productResponse != null) {
            String log = AutoRestackService.concatEndResult(
                asset,
                String.valueOf(amount),
                projectStakingId,
                productResponse.positionId(),
                productResponse.success()
            );
            slackService.sendMessage(log, SlackMessageType.AUTO_STAKING);
            logger.info(log);
        } else {
            logger.error("Erreur durant le postStaking [{}] [{}] [{}]", projectStakingId, amount, asset);
        }
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

}
