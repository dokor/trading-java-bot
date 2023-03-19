package fr.lelouet.services.external.binance.market.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderTypes {
    LIMIT,
    LIMIT_MAKER,
    MARKET,
    STOP_LOSS,
    STOP_LOSS_LIMIT,
    TAKE_PROFIT,
    TAKE_PROFIT_LIMIT
}
