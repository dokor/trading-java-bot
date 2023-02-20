package fr.lelouet.services.external.binance.staking.bean;

import java.util.List;

public record StakingProducts(List<ProjectStaking> stakingList) {
    public static StakingProducts of(List<ProjectStaking> stakingList) {
        return new StakingProducts(stakingList);
    }
}