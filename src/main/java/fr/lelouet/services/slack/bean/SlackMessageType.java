package fr.lelouet.services.slack.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlackMessageType {
    AUTO_STAKING("#trading-bot-antoine", "Stacking", ":banque:"),
    AUTO_REDEEM("#trading-bot-antoine", "Redeem", ":banque:"),
    INFORMATIF("#trading-bot-antoine", "Trading App Binance", ":rocket:"),
    LANCEMENT("#trading-bot-antoine", "Trading App Binance", ":rocket:")
    ;

    private final String channel;
    private final String username;
    private final String icon;
}
