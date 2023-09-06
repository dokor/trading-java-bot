package fr.lelouet.services.internal.trading.analysis;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.trade.beans.Candlestick;
import fr.lelouet.services.external.binance.trade.enums.CandlestickInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO : REFACTO + sortir les transformations communes + sortir les calculs communs + faire quelque chose pour une vérification uniques d'une pair
 */

/**
 * Service d'analyse des données de trading du RSI
 */
@Singleton
public class RSIService {

    private static final Logger logger = LoggerFactory.getLogger(RSIService.class);

    private final BinanceApi binanceApi;
    private static final int RSI_PERIOD = 14;

    @Inject
    public RSIService(
        BinanceApi binanceApi
    ) {
        this.binanceApi = binanceApi;
    }

    /**
     * Permet d'obtenir le RSI d'une paire de crypto
     *
     * @param symbol : la paire de crypto à analyser ex : BTCBUSD
     * @param candlestickInterval : la taille des bougies récupérées
     */
    public void analysisRSI(String symbol, CandlestickInterval candlestickInterval) {
        if (candlestickInterval == null) {
            candlestickInterval = CandlestickInterval.ONE_DAY;
        }
        // Récupérer les données historiques des prix pour la paire spécifique
        List<Candlestick> candlestickBars = binanceApi.getCandlestickBars(symbol, candlestickInterval);
        calculate(symbol, candlestickBars);
    }

    public static void calculate(String symbol, List<Candlestick> candlestickList) {
        // Créez une série de prix à partir des données Candlestick
        BarSeries series = convertToTimeSeries(candlestickList, symbol);

        // Créez l'indicateur de prix de clôture
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        // Calculez le RSI sur la période donnée
        RSIIndicator rsi = new RSIIndicator(closePrice, RSI_PERIOD);

        // Maintenant, vous pouvez utiliser l'indicateur RSI pour obtenir les valeurs RSI pour chaque barre (bougie)
        for (int i = 0; i < series.getBarCount(); i++) {
            // TODO Réaliser un objet de sortie lisible
            DecimalNum rsiValue = (DecimalNum) rsi.getValue(i);
            logger.info("RSI le " + series.getBar(i).getDateName() + ": " + rsiValue + " pour la paire " + symbol);
        }
    }

    public static BarSeries convertToTimeSeries(List<Candlestick> candlesticks, String symbol) {
        List<Bar> ticks = new LinkedList<>();
        for (Candlestick candlestick : candlesticks) {
            ticks.add(convertToTa4jTick(candlestick));
        }
        return new BaseBarSeries(symbol, ticks);
    }

    public static Bar convertToTa4jTick(Candlestick candlestick) {
        ZonedDateTime closeTime = getZonedDateTime(candlestick.getCloseTime());
        Duration candleDuration = Duration.ofMillis(candlestick.getCloseTime()
            - candlestick.getOpenTime());
        Num openPrice = DecimalNum.valueOf(candlestick.getOpen());
        Num closePrice = DecimalNum.valueOf(candlestick.getClose());
        Num highPrice = DecimalNum.valueOf(candlestick.getHigh());
        Num lowPrice = DecimalNum.valueOf(candlestick.getLow());
        Num volume = DecimalNum.valueOf(candlestick.getVolume());
        Num amount = DecimalNum.valueOf(candlestick.getNumberOfTrades());

        return new BaseBar(candleDuration, closeTime, openPrice, highPrice, lowPrice, closePrice, volume, amount);
    }

    public static ZonedDateTime getZonedDateTime(Long timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault());
    }
}
