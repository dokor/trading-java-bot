package fr.lelouet.services.slack.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlackMessageType {
    AUTO_STAKING(SlackChannels.TRADING_BOT.getChannel(), "Stacking", SlackIcons.BANQUE.getIconName()),
    AUTO_REDEEM(SlackChannels.TRADING_BOT.getChannel(), "Redeem", SlackIcons.BANQUE.getIconName()),
    LIQUIDITY_REDEEM(SlackChannels.TRADING_BOT.getChannel(), "Liquidity", SlackIcons.BANQUE.getIconName()),
    INFORMATIF(SlackChannels.TRADING_BOT.getChannel(), "Trading App Binance", SlackIcons.ROCKET.getIconName()),
    LANCEMENT(SlackChannels.TRADING_BOT.getChannel(), "Trading App Binance", SlackIcons.ROCKET.getIconName());

    private final String channel;
    private final String username;
    private final String icon;
}

// fixme : changer le typage des valeurs de l'enum et faire le get directement à l'utilisation