package fr.lelouet.services.external.binance.saving.bean;

import java.util.Map;

@Deprecated
public record FlexiblePosition(
    Map<String, Double> tierAnnualInterestRate,
    String annualInterestRate,
    String asset,
    String avgAnnualInterestRate,
    boolean canRedeem,
    String dailyInterestRate,
    String freeAmount,
    String freezeAmount,
    String productId,
    String productName,
    String redeemingAmount,
    String todayPurchasedAmount,
    String totalAmount,
    String totalInterest

) {
}
