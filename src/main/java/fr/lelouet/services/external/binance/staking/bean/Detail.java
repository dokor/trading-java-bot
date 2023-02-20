package fr.lelouet.services.external.binance.staking.bean;

public record Detail(
    String asset, // example: AXS
    String rewardAsset, // example: AXS
    Integer duration, // example: 90
    boolean renewable, // example: true
    String apy // example: 1.2069
) {
}
