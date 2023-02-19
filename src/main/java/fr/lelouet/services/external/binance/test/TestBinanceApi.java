package fr.lelouet.services.external.binance.test;

import fr.lelouet.services.external.binance.wallet.BinanceWalletClientApi;
import fr.lelouet.services.external.binance.wallet.bean.CoinWallet;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TestBinanceApi {

    private static final Logger logger = LoggerFactory.getLogger(TestBinanceApi.class);

    private final BinanceWalletClientApi binanceWalletClientApi;

    @Inject
    public TestBinanceApi(BinanceWalletClientApi binanceWalletClientApi) {
        this.binanceWalletClientApi = binanceWalletClientApi;
    }

    @SneakyThrows
    public void mainTest() {
        logger.info("------------- Lancement des tests principaux -------------");
        List<CoinWallet> brCoinWallets = binanceWalletClientApi.getAllCoinsOfConnectedUser();
        CoinsWalletInformations coinsWalletInformations = new CoinsWalletInformations(brCoinWallets);
        coinsWalletInformations.getCoinsNotZero().forEach(coinWallet -> logger.info(coinWallet.toCloseString()));
//        logger.info("Retour Api getAll {}", coinsWalletInformations.getFreeCoins());
        logger.info("------------- Fin des tests principaux -------------");
    }

    /**
     * TODO :
     * - Récupérer le portefeuille des monnaies earn stackés
     * - Récupérer le stacking disponible pour une monnaie
     * - Faire un scheduler qui indique les monnaies qui peuvent etre stackés
     *   - Fonction qui crée directement le stacking ?
     *   - alerting par slack ou Mail des derniers trades effectués
     *
     */
}
