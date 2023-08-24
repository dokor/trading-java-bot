package fr.lelouet.services.internal.history;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.trade.PastOrder;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.enums.SlackMessageType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.*;

@Singleton
public class TradeHistoryService {

    private final BinanceApi binanceApi;
    private final SlackService slackService;

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
            BinanceApi binanceApi,
            SlackService slackService
    ) {
        this.binanceApi = binanceApi;
        this.slackService = slackService;
    }

    public Map<String, List<PastOrder>> getFullHistory() {
        Map<String, List<PastOrder>> map = new HashMap<>();

        for (String crypto : CRYPTO) {
            for (String fiat : FIAT) {
                String symbol = crypto + fiat;
                map.put(symbol, binanceApi.getTradeHistory(symbol));
            }
        }
        this.logHistory(map);
        return map;
    }

    public void logHistory(Map<String, List<PastOrder>> history) {
        slackService.sendMessage("[DEBUT Historique des trades]\n", SlackMessageType.INFORMATIF);
        List<PastOrder> pastOrdersSorted = new ArrayList<>();
        for (List<PastOrder> pastOrders : history.values()) {
            pastOrdersSorted.addAll(pastOrders);
        }
        pastOrdersSorted.stream()
                .sorted(Comparator.comparing(PastOrder::symbol).thenComparingLong(PastOrder::time))
                .forEach(pastOrder -> slackService.sendMessage(cleanPastOrder(pastOrder), SlackMessageType.INFORMATIF));
        slackService.sendMessage("[FIN Historique des trades]\n", SlackMessageType.INFORMATIF);
    }

    private String cleanPastOrder(PastOrder pastOrder) {
        return cleanSymbol(pastOrder.symbol())
                + " [" + pastOrder.side() + "]"
                + " [" + pastOrder.type() + "]"
                + ": " + pastOrder.executedQty() + " à "
                + pastOrder.price() + "$ "
                + " (" + pastOrder.price() * pastOrder.executedQty() + "$)"
                + " le " + Instant.ofEpochMilli(pastOrder.time())
                ;
    }

    private String cleanSymbol(String symbol) {
        return symbol.replace("BUSD", "").replace("USDT", "");
    }
}
