package fr.lelouet.services.slack.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SlackChannels {
    TRADING_BOT("#trading-bot-antoine")
    ;

    private final String channel;
}
