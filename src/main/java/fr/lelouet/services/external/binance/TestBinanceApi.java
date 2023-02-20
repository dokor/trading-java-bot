package fr.lelouet.services.external.binance;

import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.external.binance.staking.bean.PersonalLeftQuota;
import fr.lelouet.services.external.binance.staking.bean.ProductResponse;
import fr.lelouet.services.external.binance.staking.bean.StakingProducts;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import fr.lelouet.services.slack.SlackService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class de test pour préparer la mise en place des algos
 */
@Singleton
public class TestBinanceApi {

    private static final Logger logger = LoggerFactory.getLogger(TestBinanceApi.class);
    private final BinanceApi binanceApi;
    private final ConfigurationService configurationService;
    private final SlackService slackService;

    @Inject
    public TestBinanceApi(
        BinanceApi binanceApi,
        ConfigurationService configurationService,
        SlackService slackService
    ) {
        this.binanceApi = binanceApi;
        this.configurationService = configurationService;
        this.slackService = slackService;
    }

    @SneakyThrows
    public void mainTest() {
        logger.info("------------- Lancement des tests principaux -------------");
        this.automatiqueReStack();

        logger.info("------------- Fin des tests principaux -------------");
    }

    /**
     * Fonction global de restack automatique
     * Les cryptos dispos dans stack doivent etre directement stackés lorsqu'elles sont disponible
     */
    private void automatiqueReStack() {
        // Récupération des cryptos depuis le compte spot de l'utilisateur
        CoinsWalletInformations coinsWalletInformations = binanceApi.getCoinsInformationsOfSpotWallet();

        // Coins dispos pour le stacking
        coinsWalletInformations.getCoinsAvalaible()
            .stream()
            // Filtre les cryptos volontairement à ignorer
            .filter(coinWallet -> configurationService.ignoreAutoStakingCryptoList().contains(coinWallet.coin().name()))
            // Pour chaque crypto disponible
            .forEach(coinWallet -> {
                logger.info("Stacking à identifier pour [{}] d'un montant de [{}]", coinWallet.coin(), coinWallet.free());
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
                        logger.info("[{}] d'un montant [{}] pour le produit [{}] avec une position [{}] : [{}]",
                            coinWallet.coin().name(),
                            coinWallet.free(),
                            projectStaking.projectId(),
                            productResponse.positionId(),
                            productResponse.success()
                        );
                    });
            });

        // Todo
        // Regarder ls cryptos en stacking flexible
        // Regarder s'il y a un stacking non flex de cette crypto => dispo + montant minimum
        // Rappatrié le flex dans spot et déclancher l'automatiqueRestack

        // TODO
        // Pour chaque crypto dispo
        // Regarder s'il y a un stacking associé
        // Si stacking dispo, regarder si le montant mini est OK
        // Si montant OK => Faire le trade + envoyer une mail/slack
    }
}
