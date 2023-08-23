package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import fr.lelouet.services.external.binance.trade.PastOrder;
import fr.lelouet.services.internal.history.TradeHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceTestRunner.class)
@GuiceModules(TestModule.class)
public class TradeHistoryServiceTest {

    @Inject
    TradeHistoryService tradeHistoryService;

    @Test
    public void start() {
        Map<String, List<PastOrder>> fullHistory = tradeHistoryService.getFullHistory();
        assertThat(true).isEqualTo(true);
    }
}
