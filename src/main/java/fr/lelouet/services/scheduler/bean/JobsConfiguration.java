package fr.lelouet.services.scheduler.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class JobsConfiguration {
    private String cronAutoRestack;
    private String cronDestackFlex;
}
