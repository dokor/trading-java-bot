package fr.lelouet.services.external.binance;

import fr.lelouet.services.external.binance.wallet.BinanceWalletClientApi;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.bean.SlackMessageType;
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
    private final SlackService slackService;

    @Inject
    public TestBinanceApi(
        BinanceApi binanceApi,
        SlackService slackService
    ) {
        this.binanceApi = binanceApi;
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
        // Log le contenu interessants
        coinsWalletInformations.getCoinsNotZero().forEach(coinWallet -> {
            logger.info(coinWallet.toCloseString());
            slackService.sendMessage(coinWallet.toCloseString(), SlackMessageType.INFORMATIF);
        });

        // Coins dispos pour le stacking
        coinsWalletInformations.getCoinsAvalaible()
            .forEach(coinWallet -> logger.info("Chercher un stacking pour {} : {}", coinWallet.coin(), coinWallet.free()));

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
