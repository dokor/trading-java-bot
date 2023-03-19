package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import fr.lelouet.services.external.binance.TestBinanceApi;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceTestRunner.class)
@GuiceModules(TestModule.class)
public class TestService {

    @Inject
    TestBinanceApi testBinanceApi;

    @Test
    public void start() {
        testBinanceApi.mainTest();
        assertThat(true).isEqualTo(true);
    }
}
