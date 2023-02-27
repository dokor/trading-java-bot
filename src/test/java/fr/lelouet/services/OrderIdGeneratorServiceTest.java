package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(GuiceTestRunner.class)
@GuiceModules(TestModule.class)
public class OrderIdGeneratorServiceTest {

    final int numberOfThreads = 100;

    /**
     *
     */
    @Test
    public void testCounterWithConcurrency() throws InterruptedException {

    }

    @Test
    public void testCreationTwoOrders() {

    }
}
