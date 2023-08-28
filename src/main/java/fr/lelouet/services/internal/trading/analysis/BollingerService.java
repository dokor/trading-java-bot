package fr.lelouet.services.internal.trading.analysis;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.trade.beans.Candlestick;
import fr.lelouet.services.external.binance.trade.enums.CandlestickInterval;
import fr.lelouet.services.external.binance.wallet.BinanceWalletClientApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Singleton
public class BollingerService {

    private static final Logger logger = LoggerFactory.getLogger(BollingerService.class);

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

        // Créer une série de bougies TA4j
        BaseBarSeries series = new BaseBarSeries();

        for (Candlestick candlestick : candlestickBars) {
//            BaseBar bar = BaseBar.builder(DecimalNum::valueOf, Number.class)
//                .timePeriod(Duration.ofDays(1)) // adapter avec le CandlestickInterval
//                .endTime(ZonedDateTime.of(candlestick.getCloseTime()))
//                .openPrice(Double.valueOf(candlestick.getOpen()))
//                .highPrice(Double.valueOf(candlestick.getHigh()))
//                .lowPrice(Double.valueOf(candlestick.getLow()))
//                .closePrice(Double.valueOf(candlestick.getClose()))
//                .volume(candlestick.getVolume())
//                .build();
//            series.addBar(bar);
        }

//
//        logger.info("Valeur Bollinger Bands Upper : " + upperValue);
//        logger.info("Valeur Bollinger Bands Lower : " + lowerValue);

        // Interpréter les résultats et prendre des décisions
        // ... (analysez les indicateurs pour prendre des décisions de trading)
    }
}