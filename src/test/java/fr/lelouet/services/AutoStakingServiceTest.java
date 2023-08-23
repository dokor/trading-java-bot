package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import fr.lelouet.services.internal.staking.AutoRestackService;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lancement de l'autoStacking
 * Le principe :
 *  - Déstacker le staking flexible afin de prévoir un staking non flexible plus intéréssant.
 *  - Stacker les cryptos de spots dans des staking non flexible.
 *
 *  PI :
 *  - Choisi les taux plus intéréssants.
 *  - Vérifie que le staking est possible avant de destacker
 */
@RunWith(GuiceTestRunner.class)
@GuiceModules(TestModule.class)
public class AutoStakingServiceTest {

    @Inject
    AutoRestackService autoRestackService;

    @Test
    public void start() {
        autoRestackService.redeemFlexibleStaking();
        autoRestackService.automaticReStack();
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void redeemFlexibleStaking(){
        autoRestackService.redeemFlexibleStaking();
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void automaticReStack(){
        autoRestackService.automaticReStack();
        assertThat(true).isEqualTo(true);
    }
}
