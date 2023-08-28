package fr.lelouet.services.external.binance.wallet.bean;

import java.util.List;
import java.util.Map;

public record TradingFeeResponse(
    List<TradingFee> tradeFees
) {

    public Map<String, TradingFee> transform() {
        return tradeFees.stream()
            .collect(
                java.util.stream.Collectors.toMap(
                    TradingFee::symbol,
                    (tf) -> tf
                )
            );
    }
}
