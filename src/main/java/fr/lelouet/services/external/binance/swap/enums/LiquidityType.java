package fr.lelouet.services.external.binance.swap.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LiquidityType {
    SWAP_REWARDS(0), // default
    LIQUIDITY_REWARDS(1);
    private final int value;
}
