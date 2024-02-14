package fr.lelouet.services.external.binance.saving.bean;

public record FlexiblePositionByAsset(
    String asset,
    String latestAnnualPercentageRate,
    String totalAmount,
    String airDropPercentageRate,
    Boolean canPurchase,
    Boolean canRedeem,
    Boolean isSoldOut,
    Boolean hot,
    String minPurchaseAmount,
    String productId,
    String subscriptionStartTime,
    String status
) {
}
