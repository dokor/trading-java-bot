package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lelouet.guice.TestModule;
import fr.lelouet.services.external.binance.TestBinanceApi;
import fr.lelouet.services.external.binance.trade.beans.Candlestick;
import fr.lelouet.services.internal.trading.analysis.RSIService;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceTestRunner.class)
@GuiceModules(TestModule.class)
public class TestService {

    @Inject
    TestBinanceApi testBinanceApi;

    @Inject
    RSIService rsiService;

    @Test
    public void start() {
        rsiService.analysisRSI("BTCBUSD", null);
        assertThat(true).isEqualTo(true);
    }
}
