package fr.lelouet.services.external.binance.saving.bean;

import java.util.Map;

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

// MOCK EXEMPLE
//    {
//        "tierAnnualInterestRate":{
//        "0-5BTC":0.05,
//        "5-10BTC":0.03,
//        ">10BTC":0.01
//        },
//        "annualInterestRate":"0.02599895",
//        "asset":"USDT",
//        "avgAnnualInterestRate":"0.02599895",
//        "canRedeem":true,
//        "dailyInterestRate":"0.00007123",
//        "freeAmount":"75.46000000",
//        "freezeAmount":"0.00000000", // abandoned
//        "lockedAmount":"0.00000000", // abandoned
//        "productId":"USDT001",
//        "productName":"USDT",
//        "redeemingAmount":"0.00000000",
//        "todayPurchasedAmount":"0.00000000",
//        "totalAmount":"75.46000000",
//        "totalInterest":"0.22759183"
//        }

