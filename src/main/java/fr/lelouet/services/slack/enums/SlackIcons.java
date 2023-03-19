package fr.lelouet.services.slack.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SlackIcons {
    BANQUE(":banque:"),
    ROCKET(":rocket:")
    ;

    private final String iconName;
}
