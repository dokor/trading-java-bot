package fr.lelouet.services.external.binance.trade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NewOrderResponseType {
    JSON,
    ACK,
    RESULT, // default
    FULL;
}
