package fr.lelouet.services.external.binance.swap.bean;

import java.util.Map;

public record LiquidityRewards(
    Map<String, Double> totalUnclaimedRewards
    // details ingoré pour le moment
) {
}