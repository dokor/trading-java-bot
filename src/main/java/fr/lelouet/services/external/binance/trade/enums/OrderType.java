package fr.lelouet.services.external.binance.trade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderType {
    LIMIT , //	timeInForce, quantity, price
    MARKET , //	quantity or quoteOrderQty
    STOP_LOSS , //	quantity, stopPrice or trailingDelta
    STOP_LOSS_LIMIT , //	timeInForce, quantity, price, stopPrice or trailingDelta
    TAKE_PROFIT , //	quantity, stopPrice or trailingDelta
    TAKE_PROFIT_LIMIT , //	timeInForce, quantity, price, stopPrice or trailingDelta
    LIMIT_MAKER  //	quantity, price
    ;
}
