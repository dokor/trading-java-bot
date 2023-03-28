package fr.lelouet.services.external.binance.trade;

import com.binance.connector.client.impl.spot.Trade;
import fr.lelouet.services.external.binance.config.enums.BinanceQueryParam;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import fr.lelouet.services.external.binance.wallet.bean.CoinWallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;

@Singleton
public class BinanceTradeClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceTradeClientApi.class);

    private final Trade client;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceTradeClientApi(
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.client = binanceGlobalProvider.getSpotClient().createTrade();
    }

    public CoinWallet getOrderBook(String pair) {
        logger.debug("Tentative de récupération de l'orderBook de la pair [{}]", pair);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.ASSET.getValue(), pair);
        CoinWallet[] coinWallets = binanceGlobalProvider.callBinanceApi(client, "depth", CoinWallet[].class, stringObjectLinkedHashMap);
        if (coinWallets != null && coinWallets.length > 0) {
            return coinWallets[0];
        }
        return null;
    }

    // TODO implements
    // GET /api/v3/depth => Récupere l'orderBook
    // GET /api/v3/ticker/bookTicker => Best price/qty on the order book for a symbol or symbols.

}
