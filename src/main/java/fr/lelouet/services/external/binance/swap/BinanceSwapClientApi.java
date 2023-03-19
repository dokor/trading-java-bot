package fr.lelouet.services.external.binance.swap;

import com.binance.connector.client.impl.spot.BSwap;
import fr.lelouet.services.external.binance.config.enums.BinanceQueryParam;
import fr.lelouet.services.external.binance.swap.bean.ClaimRewardResponse;
import fr.lelouet.services.external.binance.swap.bean.LiquidityRewards;
import fr.lelouet.services.external.binance.swap.enums.LiquidityType;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;

@Singleton
public class BinanceSwapClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceSwapClientApi.class);

    private final BSwap client;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceSwapClientApi(
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.client = binanceGlobalProvider.getSpotClient().createBswap();
    }

    public LiquidityRewards getUnclaimRewards() {
        logger.debug("Tentative de récupération des liquidity rewards");
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.TYPE.getValue(), LiquidityType.LIQUIDITY_REWARDS.getValue());
        return binanceGlobalProvider.callBinanceApi(client, "unclaimedRewards", LiquidityRewards.class, stringObjectLinkedHashMap);
    }

    public ClaimRewardResponse claimRewards() {
        logger.debug("Tentative de claim les liquidity rewards");
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.TYPE.getValue(), LiquidityType.LIQUIDITY_REWARDS.getValue());
        return binanceGlobalProvider.callBinanceApi(client, "claimRewards", ClaimRewardResponse.class, stringObjectLinkedHashMap);
    }
}
