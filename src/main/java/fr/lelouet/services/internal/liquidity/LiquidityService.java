package fr.lelouet.services.internal.liquidity;

import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.swap.bean.ClaimRewardResponse;
import fr.lelouet.services.external.binance.swap.bean.LiquidityRewards;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.enums.SlackMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LiquidityService {

    private static final Logger logger = LoggerFactory.getLogger(LiquidityService.class);
    private final BinanceApi binanceApi;
    private final SlackService slackService;

    @Inject
    public LiquidityService(
        BinanceApi binanceApi,
        SlackService slackService
    ) {
        this.binanceApi = binanceApi;
        this.slackService = slackService;
    }

    public Boolean redeemLiquidityReward() {
        LiquidityRewards liquidityRewards = binanceApi.getUnclaimRewards();
        logger.debug("Récupération de la liste des récompenses à récupérer : [{}]", liquidityRewards.totalUnclaimedRewards());
        ClaimRewardResponse claimRewardResponse = binanceApi.claimRewards();
        if (Boolean.TRUE.equals(claimRewardResponse.success())) {
            String message = "[LIQUIDITY_REDEEM] Les liquidités ont bien été récupérées !"
                + liquidityRewards.totalUnclaimedRewards();
            slackService.sendMessage(message, SlackMessageType.LIQUIDITY_REDEEM);
            logger.info("La récupération des liquidity à récupérer est réussi !");
        }
        return claimRewardResponse.success();

    }
}
