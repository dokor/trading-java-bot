package fr.lelouet.services.internal.trading.analysis;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.trade.beans.Candlestick;
import fr.lelouet.services.external.binance.trade.enums.CandlestickInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
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
    private static final int UPPER_LIMIT = 70;
    private static final int LOWER_LIMIT = 30;

    @Inject
    public RSIService(
        BinanceApi binanceApi
    ) {
        this.binanceApi = binanceApi;
    }

    /**
     * Permet d'obtenir le RSI d'une paire de crypto
     *
     * @param symbol              : la paire de crypto à analyser ex : BTCBUSD
     * @param candlestickInterval : la taille des bougies récupérées
     * @return
     */
    public List<Bar> analysisRSI(String symbol, @Nullable CandlestickInterval candlestickInterval) {
        if (candlestickInterval == null) {
            candlestickInterval = CandlestickInterval.ONE_DAY;
        }
        // Récupérer les données historiques des prix pour la paire spécifique
        List<Candlestick> candlestickBars = binanceApi.getCandlestickBars(symbol, candlestickInterval);
        return calculate(symbol, candlestickBars);
    }

    public static List<Bar> calculate(String symbol, List<Candlestick> candlestickList) {
        // Créez une série de prix à partir des données Candlestick
        BarSeries series = IndicateursUtilsService.convertToTimeSeries(candlestickList, symbol);

        // Créez l'indicateur de prix de clôture
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        // Calculez le RSI sur la période donnée
        RSIIndicator rsi = new RSIIndicator(closePrice, RSI_PERIOD);
        List<Bar> barsToStudy = new ArrayList<>();
        // Maintenant, vous pouvez utiliser l'indicateur RSI pour obtenir les valeurs RSI pour chaque barre (bougie)
        for (int i = 0; i < series.getBarCount(); i++) {
            // TODO Réaliser un objet de sortie lisible
            DecimalNum rsiValue = (DecimalNum) rsi.getValue(i);
            if (rsiValue.doubleValue() > UPPER_LIMIT || rsiValue.doubleValue() < LOWER_LIMIT) {
                barsToStudy.add(series.getBar(i));
                if (i > 1 && i < series.getBarCount() - 1) {
//                    tempLog(series.getBar(i - 1), series.getBar(i), series.getBar(i + 1));
                }
                logger.info("RSI le " + series.getBar(i).getDateName() + ": " + rsiValue + " pour la paire " + symbol);
            } else {
                logger.debug("RSI le " + series.getBar(i).getDateName() + ": " + rsiValue + " pour la paire " + symbol);
            }
            logger.info("Prix du bar courant : [{}] // [{}] // [{}] // [{}] ",
                series.getBar(i).getOpenPrice(),
                series.getBar(i).getHighPrice(),
                series.getBar(i).getLowPrice(),
                series.getBar(i).getClosePrice()
            );
        }
        return barsToStudy;
    }

    private static void tempLog(Bar beforeBar, Bar bar, Bar afterBar) {
        logger.warn("Prix du bar d'avant : [{}] // [{}] // [{}] // [{}] ",
            beforeBar.getOpenPrice(),
            beforeBar.getHighPrice(),
            beforeBar.getLowPrice(),
            beforeBar.getClosePrice()
        );
        logger.warn("Prix du bar actuel avec le RSI : [{}] // [{}] // [{}] // [{}] ",
            bar.getOpenPrice(),
            bar.getHighPrice(),
            bar.getLowPrice(),
            bar.getClosePrice()
        );
        logger.warn("Prix du bar d'aprés : [{}] // [{}] // [{}] // [{}] ",
            afterBar.getOpenPrice(),
            afterBar.getHighPrice(),
            afterBar.getLowPrice(),
            afterBar.getClosePrice()
        );
    }
}
