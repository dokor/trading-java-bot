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
    ASSET("asset");
    private final String value;
}
