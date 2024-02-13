package fr.lelouet.services.internal.trading.analysis;

import fr.lelouet.services.external.binance.trade.beans.Candlestick;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
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

@Singleton
public class IndicateursUtilsService {

    @Inject
    public IndicateursUtilsService() {
        // this constructor is empty
    }

    public static BarSeries convertToTimeSeries(List<Candlestick> candlesticks, String symbol) {
        List<Bar> ticks = new LinkedList<>();
        for (Candlestick candlestick : candlesticks) {
            ticks.add(convertToTa4jTick(candlestick));
        }
        return new BaseBarSeries(symbol, ticks);
    }

    private static Bar convertToTa4jTick(Candlestick candlestick) {
        ZonedDateTime closeTime = getZonedDateTime(candlestick.getCloseTime());
        Duration candleDuration = Duration.ofMillis(candlestick.getCloseTime() - candlestick.getOpenTime());
        Num openPrice = DecimalNum.valueOf(candlestick.getOpen());
        Num closePrice = DecimalNum.valueOf(candlestick.getClose());
        Num highPrice = DecimalNum.valueOf(candlestick.getHigh());
        Num lowPrice = DecimalNum.valueOf(candlestick.getLow());
        Num volume = DecimalNum.valueOf(candlestick.getVolume());
        Num amount = DecimalNum.valueOf(candlestick.getNumberOfTrades());

        return new BaseBar(candleDuration, closeTime, openPrice, highPrice, lowPrice, closePrice, volume, amount);
    }

    private static ZonedDateTime getZonedDateTime(Long timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault());
    }
}
