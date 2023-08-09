package fr.lelouet.services.lauchers;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import fr.lelouet.services.internal.liquidity.LiquidityService;
import fr.lelouet.services.internal.staking.AutoRestackService;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lancement de l'autoStacking
 * Le principe :
 * - Déstacker le staking flexible afin de prévoir un staking non flexible plus intéréssant.
 * - Stacker les cryptos de spots dans des staking non flexible.
 * <p>
 * PI :
 * - Choisi les taux plus intéréssants.
 * - Vérifie que le staking est possible avant de destacker
 */
@RunWith(GuiceTestRunner.class)
@GuiceModules(TestModule.class)
public class GlobalLauncherService {

    @Inject
    AutoRestackService autoRestackService;

    @Inject
    LiquidityService liquidityService;

    @Test
    public void start() {
        liquidityService.redeemLiquidityReward();
        autoRestackService.redeemFlexibleStaking();
        autoRestackService.automaticReStack();
        assertThat(true).isEqualTo(true);
    }
}
