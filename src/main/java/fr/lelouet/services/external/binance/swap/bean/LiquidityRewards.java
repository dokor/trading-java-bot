package fr.lelouet.services.external.binance.swap.bean;

import java.util.Map;

public record LiquidityRewards(
    Map<String, Double> totalUnclaimedRewards
    // details ingoré pour le moment
) {
}


//{
//    "totalUnclaimedRewards": {
//    "BUSD": 100000315.79,
//    "BNB": 0.00000001,
//    "USDT": 0.00000002
//    },
//    "details":{
//    "BNB/USDT":{
//    "BUSD": 100000315.79,
//    "USDT": 0.00000002
//    },
//    "BNB/BTC":{
//    "BNB": 0.00000001
//    }
//    }
//    }