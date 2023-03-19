package fr.lelouet.services.external.binance.market;

import com.binance.connector.client.impl.spot.Market;

import fr.lelouet.services.external.binance.config.enums.BinanceQueryParam;
import fr.lelouet.services.external.binance.market.beans.OrderBook;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;

@Singleton
public class BinanceMarketClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceMarketClientApi.class);

    private final Market clientMarket;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceMarketClientApi(
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.clientMarket = binanceGlobalProvider.getSpotClient().createMarket();
    }

    public OrderBook getOrderBookOfSymbol(String symbol) {
        logger.debug("Tentative de récupération des staking products [{}] du user courant", symbol);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.SYMBOL.getValue(), symbol);
        return binanceGlobalProvider.callBinanceApi(clientMarket, "depth", OrderBook.class, stringObjectLinkedHashMap);
    }

    // TODO implements
    // GET /api/v3/ticker/bookTicker => Best price/qty on the order book for a symbol or symbols.

}
