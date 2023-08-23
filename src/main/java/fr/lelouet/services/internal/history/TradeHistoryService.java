package fr.lelouet.services.internal.history;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.trade.PastOrder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class TradeHistoryService {

    private final BinanceApi binanceApi;

    private static final List<String> FIAT = List.of("BUSD", "USDT");
    private static final List<String> CRYPTO = List.of(
        "BTC",
        "ETH",
        "ADA",
        "AVAX",
        "BNB",
        "VET",
        "DOT",
        "SOL"
    );

    @Inject
    public TradeHistoryService(
        BinanceApi binanceApi
    ) {
        this.binanceApi = binanceApi;
    }

    public Map<String, List<PastOrder>> getFullHistory() {
        Map<String, List<PastOrder>> map = new HashMap<>();

        for (String crypto : CRYPTO) {
            for (String fiat : FIAT) {
                String symbol = crypto + fiat;
                map.put(symbol, binanceApi.getTradeHistory(symbol));
            }
        }
        return map;
    }
}
