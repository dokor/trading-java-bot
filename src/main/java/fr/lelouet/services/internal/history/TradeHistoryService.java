package fr.lelouet.services.internal.history;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.trade.PastOrder;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.enums.SlackMessageType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class TradeHistoryService {

    private final BinanceApi binanceApi;
    private final SlackService slackService;

    private static final List<String> FIAT = List.of(
        "BUSD",
        "USDT"
    );
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

    /**
     * Récupération l'historique complet des trades réalisés sur les cryptos listés dans CRYPTO
     * avec les FIAT listés
     */
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
        // todo : opti
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

    /**
     * Netoyage du trade pour affichage dans un log informatif
     */
    private String cleanPastOrder(PastOrder pastOrder) {
        return cleanSymbol(pastOrder.symbol())
            + " [" + pastOrder.side() + "]"
            + " [" + pastOrder.type() + "]"
            + ": " + pastOrder.executedQty() + " à "
            + pastOrder.price() + "$ "
            + " (" + calculatePrice(pastOrder.price(), pastOrder.executedQty()) + "$)"
            + " le " + Instant.ofEpochMilli(pastOrder.time())
            ;
    }

    /**
     * Calcul du prix d'un trade et réduction de l'arrondi pour affiche de 2 chiffres après la virgule
     */
    private String calculatePrice(Double price, Double qty) {
        BigDecimal prixBigDecimal = new BigDecimal(price * qty);
        BigDecimal prixArrondi = prixBigDecimal.setScale(2, RoundingMode.HALF_UP);
        return prixArrondi + "$";
    }

    /**
     * Netoyage du symbol du trade pour connaitre la crypto
     */
    private String cleanSymbol(String symbol) {
        for (String s : FIAT) {
            if (symbol.contains(s)) {
                return symbol.replace(s, "");
            }
        }
        return symbol;
    }
}
