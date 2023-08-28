package fr.lelouet.services.external.binance.wallet.bean;

public record TradingFee(
    String symbol,
    Double makerCommission,
    Double takerCommission
) {
}
