package fr.lelouet.services.external.binance.staking.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class StakingProducts {
    List<ProjectStaking> stakingList;

    public List<ProjectStaking> orderByApy() {
        return this.stakingList
            .stream()
            .sorted((o1, o2) -> {
                Double apyA = Double.valueOf(o1.detail().apy());
                Double apyB = Double.valueOf(o2.detail().apy());
                return apyB.compareTo(apyA);
            })
            .toList();

    }
}