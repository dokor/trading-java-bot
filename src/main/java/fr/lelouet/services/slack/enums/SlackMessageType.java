package fr.lelouet.services.slack.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlackMessageType {
    AUTO_STAKING(SlackChannels.TRADING_BOT_ANTOINE.getChannel(), "Stacking", SlackIcons.BANQUE.getIconName()),
    AUTO_REDEEM(SlackChannels.TRADING_BOT_ANTOINE.getChannel(), "Redeem", SlackIcons.BANQUE.getIconName()),
    LIQUIDITY_REDEEM(SlackChannels.TRADING_BOT_ANTOINE.getChannel(), "Liquidity", SlackIcons.BANQUE.getIconName()),
    INFORMATIF(SlackChannels.TRADING_BOT_ANTOINE.getChannel(), "Trading App Binance", SlackIcons.ROCKET.getIconName()),
    LANCEMENT(SlackChannels.TRADING_BOT_ANTOINE.getChannel(), "Trading App Binance", SlackIcons.ROCKET.getIconName());

    private final String channel;
    private final String username;
    private final String icon;
}