package fr.lelouet.services.slack.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SlackChannels {
    TRADING_BOT_ANTOINE("#trading-bot-antoine"),
    TRADING_BOT_INFO("#trading-bot-information")
    ;

    private final String channel;
}
