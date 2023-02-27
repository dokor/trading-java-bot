package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class tests de la génération d'id de commande.
 * L'objectif est de vérifier l'unicité d'un numéro de commande malgrés une création multiple au meme timestamp
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
