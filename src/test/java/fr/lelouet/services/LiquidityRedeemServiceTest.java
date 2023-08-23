package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import fr.lelouet.services.internal.liquidity.LiquidityService;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceTestRunner.class)
@GuiceModules(TestModule.class)
public class LiquidityRedeemServiceTest {

    @Inject
    LiquidityService liquidityService;

    @Test
    public void start() {
        liquidityService.redeemLiquidityReward();
        assertThat(true).isTrue();
    }
}
