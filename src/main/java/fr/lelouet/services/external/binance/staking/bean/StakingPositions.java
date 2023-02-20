package fr.lelouet.services.external.binance.staking.bean;

import java.util.List;

public record StakingPositions(List<StakingPosition> positionList) {
    public static StakingPositions of(List<StakingPosition> toList) {
        return new StakingPositions(toList);
    }
}
