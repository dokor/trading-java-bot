package fr.lelouet.services.external.binance;

import fr.lelouet.services.external.binance.trade.PastOrder;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Class de test pour préparer la mise en place des algos
 */
@Singleton
public class TestBinanceApi {

    private static final Logger logger = LoggerFactory.getLogger(TestBinanceApi.class);
    private final BinanceApi binanceApi;

    @Inject
    public TestBinanceApi(
        BinanceApi binanceApi
    ) {
        this.binanceApi = binanceApi;
    }

    @SneakyThrows
    public void mainTest() {
        logger.info("------------- Lancement des tests principaux -------------");
        List<PastOrder> pastOrdersBTCUSDT = binanceApi.getTradeHistory("BTCUSDT");
        logger.info("------------- Fin des tests principaux -------------");
    }

    //BOT INTERESSANT : https://trading-bot.cassandre.tech/fr/


}
