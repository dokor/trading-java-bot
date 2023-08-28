package fr.lelouet.services.internal.trading.analysis;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.trade.beans.Candlestick;
import fr.lelouet.services.external.binance.trade.enums.CandlestickInterval;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BollingerService {

    private final BinanceApi binanceApi;

    @Inject
    public BollingerService(
        BinanceApi binanceApi
    ) {
        this.binanceApi = binanceApi;
    }

    // TODO
    public void analysisBollinger(String symbol) {
        // Récupérer les données historiques des prix pour la paire spécifique
        List<Candlestick> candlestickBars = binanceApi.getCandlestickBars(symbol, CandlestickInterval.ONE_DAY);

        // Calculer les bandes de Bollinger
        // ... (utilisez des bibliothèques ou implémentez le calcul des indicateurs)

        // Interpréter les résultats et prendre des décisions
        // ... (analysez les indicateurs pour prendre des décisions de trading)
    }
}