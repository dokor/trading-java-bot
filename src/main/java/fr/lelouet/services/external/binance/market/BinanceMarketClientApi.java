package fr.lelouet.services.external.binance.market;

import com.binance.connector.client.impl.spot.Market;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lelouet.services.external.binance.config.enums.BinanceQueryParam;
import fr.lelouet.services.external.binance.market.beans.OrderBook;
import fr.lelouet.services.external.binance.market.beans.TickerPrice;
import fr.lelouet.services.external.binance.trade.beans.Candlestick;
import fr.lelouet.services.external.binance.trade.enums.CandlestickInterval;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Singleton
public class BinanceMarketClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceMarketClientApi.class);

    private final Market clientMarket;
    private final BinanceGlobalProvider binanceGlobalProvider;
    private final ObjectMapper objectMapper;

    @Inject
    public BinanceMarketClientApi(
        ObjectMapper objectMapper,
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.objectMapper = objectMapper;
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.clientMarket = binanceGlobalProvider.getSpotClient().createMarket();
    }

    public OrderBook getOrderBookOfSymbol(String symbol) {
        logger.debug("Tentative de récupération des staking products [{}] du user courant", symbol);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.SYMBOL.getValue(), symbol);
        return binanceGlobalProvider.callBinanceApi(clientMarket, "depth", OrderBook.class, stringObjectLinkedHashMap);
    }

    public TickerPrice getAveragePrice(String symbol) {
        logger.debug("Tentative de récupération du prix moyen de la pair [{}]", symbol);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.SYMBOL.getValue(), symbol);
        return binanceGlobalProvider.callBinanceApi(clientMarket, "averagePrice", TickerPrice.class, stringObjectLinkedHashMap);
    }

    @SneakyThrows
    public List<Candlestick> getCandlestickBars(String symbol, CandlestickInterval candlestickInterval) {
        logger.debug("Tentative de récupération des candles de la pair [{}] sur l'interval [{}]", symbol, candlestickInterval.name());
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.SYMBOL.getValue(), symbol);
        stringObjectLinkedHashMap.put(BinanceQueryParam.INTERVAL.getValue(), candlestickInterval.getIntervalId());
        stringObjectLinkedHashMap.put(BinanceQueryParam.LIMIT.getValue(), 1000); // default : 500, max : 1000
        String objects = binanceGlobalProvider.callBinanceApi(clientMarket, "klines", String.class, stringObjectLinkedHashMap);
        if (objects != null) {
            return transform(objects);
        }
        return Collections.emptyList();
    }


    /**
     * todo : refacto pour faire une désérialisation plus propre
     * Permet de transformer le retour de l'api en une list de Candlestick
     */
    private List<Candlestick> transform(String data) throws JsonProcessingException {
        List<List<Object>> objects = objectMapper.readValue(data, objectMapper.getTypeFactory().constructCollectionType(List.class, objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class)));

        List<Candlestick> candlesticks = new ArrayList<>();

        for (List<Object> item : objects) {
            Candlestick candlestick = new Candlestick();
            candlestick.setOpenTime((Long) item.get(0));
            candlestick.setOpen((String) item.get(1));
            candlestick.setHigh((String) item.get(2));
            candlestick.setLow((String) item.get(3));
            candlestick.setClose((String) item.get(4));
            candlestick.setVolume((String) item.get(5));
            candlestick.setCloseTime((Long) item.get(6));
            candlestick.setQuoteAssetVolume((String) item.get(7));
            candlestick.setNumberOfTrades((Integer) item.get(8));
            candlestick.setTakerBuyBaseAssetVolume((String) item.get(9));
            candlestick.setTakerBuyQuoteAssetVolume((String) item.get(10));

            candlesticks.add(candlestick);
        }
        return candlesticks;
    }
}
