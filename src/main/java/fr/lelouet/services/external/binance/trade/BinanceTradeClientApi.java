package fr.lelouet.services.external.binance.trade;

import com.binance.connector.client.impl.spot.Trade;
import fr.lelouet.services.external.binance.trade.beans.NewOrder;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
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

    public Object newOrder(NewOrder newOrder) {
        logger.debug("Tentative de création d'un order [{}][{}] pour le symbol [{}]",
            newOrder.getType(),
            newOrder.getSide(),
            newOrder.getSymbol()
        );
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
//        stringObjectLinkedHashMap.put(BinanceQueryParam.ASSET.getValue(), pair);
        Object object = binanceGlobalProvider.callBinanceApi(client, "newOrder", Object.class, stringObjectLinkedHashMap);
        return object;
    }

}
