package fr.lelouet.services.internal.strategy.example;

import fr.lelouet.services.internal.strategy.Strategy;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StrategyExample implements Strategy {

    @Inject
    public StrategyExample() {

    }

    /**
     *
     */
    @Override
    public void onAccountUpdated() {

    }

    @Override
    public void onTickersUpdated() {

    }

    @Override
    public void onOrdersUpdated() {

    }

    @Override
    public void onTradesUpdated() {

    }

    @Override
    public void onPositionsUpdated() {

    }

    @Override
    public void onPositionsStatusUpdates() {

    }
}
