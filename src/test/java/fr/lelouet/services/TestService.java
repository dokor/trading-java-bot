package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import fr.lelouet.services.external.binance.TestBinanceApi;
import fr.lelouet.services.internal.trading.analysis.rsi.RSIService;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

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
