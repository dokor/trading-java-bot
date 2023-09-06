package fr.lelouet.services.external.binance.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BinanceQueryParam {
    SIZE("size"),
    PRODUCT("product"),
    PRODUCT_ID("productId"),
    AMOUNT("amount"),
    TYPE("type"),
    SYMBOL("symbol"),
    ASSET("asset"),
    START_TIME("startTime"),
    END_TIME("endTime"),
    INTERVAL("interval"),
    LIMIT("limit")
    ;
    private final String value;
}
