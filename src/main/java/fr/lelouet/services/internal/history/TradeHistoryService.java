package fr.lelouet.services.internal.history;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.market.beans.TickerPrice;
import fr.lelouet.services.external.binance.trade.beans.PastOrder;
import fr.lelouet.services.external.binance.trade.enums.OrderSide;
import fr.lelouet.services.internal.history.beans.ProfitBean;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.enums.SlackMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Sercice de gestion de l'historique des trades réalisés
 * Permet de récupérer l'historique des trades réalisés, de les analysés et de logger les informations intéréssantes
 */
@Singleton
public class TradeHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(TradeHistoryService.class);
    private final BinanceApi binanceApi;
    private final SlackService slackService;

    // Liste des FIAT utilisés pour calculer les pairs
    private static final List<String> FIAT = List.of(
        "BUSD",
        "USDT"
    );

    // Liste des Crypto qui seront récupérés dans l'historique
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
        return map;
    }

    /**
     * En cours de dev
     * // TODO : 2021-10-13 : a terminer
     */
    public Map<String, ProfitBean> calculProfitOfHistory() {
        // todo : opti
        Map<String, ProfitBean> profitBeanMap = new HashMap<>();
        Map<String, List<PastOrder>> fullHistory = this.getFullHistory();
        for (List<PastOrder> pastOrders : fullHistory.values()) {
            for (PastOrder pastOrder : pastOrders) {
                ProfitBean profitBean;
                // Création/Recupération du bean
                if (profitBeanMap.get(cleanSymbol(pastOrder.symbol())) == null) {
                    profitBean = new ProfitBean(cleanSymbol(pastOrder.symbol()), null, null);
                } else {
                    profitBean = profitBeanMap.get(cleanSymbol(pastOrder.symbol()));
                }
                // Ajout du trade dans le bean
                if (OrderSide.BUY.equals(pastOrder.side())) {
                    profitBean.addBuy(pastOrder);
                }
                if (OrderSide.SELL.equals(pastOrder.side())) {
                    profitBean.addSell(pastOrder);
                }
                profitBeanMap.put(cleanSymbol(pastOrder.symbol()), profitBean);
            }
        }

        for (ProfitBean profitBean : profitBeanMap.values()) {
            TickerPrice tickerPrice = binanceApi.getAveragePrice(profitBean.getSymbol() + "BUSD");
            double profit = profitBean.netProfitOrLoss(); // Profit déjà réalisée
            double restant = profitBean.getCurrentBalance() * tickerPrice.price(); // Stock actuel au prix courant
            if (profit >= 0) {
                logger.warn("[{}] [WIN] Trading [{}]$ et il reste un stock de [{}]$ ", profitBean.getSymbol(), profit, restant);
            } else {
                if (restant < 0) {
                    logger.warn("[{}] [LOOSE] Trading [{}]$ + un stock de [{}]$", profitBean.getSymbol(), profit, restant);
                } else {
                    if (profit + restant > 0) {
                        logger.warn("[{}] [WIN] Trading [{}]$ + un stock de [{}]$", profitBean.getSymbol(), profit, restant);
                    } else {
                        logger.warn("[{}] [LOOSE] Trading [{}]$ + un stock de [{}]$", profitBean.getSymbol(), profit, restant);
                    }
                }
            }
        }
        return profitBeanMap;
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
