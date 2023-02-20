package fr.lelouet.services.external.binance.staking.bean;

public record Quota(
    String totalPersonalQuota, //example: 2
    String minimum // example: 0.001

) {
}
