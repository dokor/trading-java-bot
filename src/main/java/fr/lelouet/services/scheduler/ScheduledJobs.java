package fr.lelouet.services.scheduler;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedules;
import com.coreoz.wisp.schedule.cron.CronSchedule;
import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.internal.liquidity.LiquidityService;
import fr.lelouet.services.internal.staking.AutoRestackService;
import fr.lelouet.services.scheduler.bean.JobsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Classe de définition des taches schedulées.
 */
@Singleton
public class ScheduledJobs {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledJobs.class);

    private final Scheduler scheduler;
    private final AutoRestackService autoRestackService;
    private final LiquidityService liquidityService;
    private final JobsConfiguration jobsConfiguration;

    @Inject
    ScheduledJobs(Scheduler scheduler,
                  ConfigurationService configurationService,
                  AutoRestackService autoRestackService,
                  LiquidityService liquidityService
    ) {
        this.scheduler = scheduler;
        this.jobsConfiguration = configurationService.getJobsConfiguration();
        this.autoRestackService = autoRestackService;
        this.liquidityService = liquidityService;
    }

    /**
     * Initialisation des taches schedulées
     */
    public void scheduleJobs() {
        if (jobsConfiguration.getCronRedeemLiquidity() != null && !jobsConfiguration.getCronRedeemLiquidity().isEmpty()) {
            scheduler.schedule(
                "Lancement du redeem des liquidity rewards",
                liquidityService::redeemLiquidityReward,
                Schedules.executeAt(jobsConfiguration.getCronRedeemLiquidity())
            );
        }
        if (jobsConfiguration.getCronDestackFlex() != null && !jobsConfiguration.getCronDestackFlex().isEmpty()) {
            scheduler.schedule(
                "Lancement du destack flexible automatique",
                autoRestackService::redeemFlexibleStaking,
                Schedules.executeAt(jobsConfiguration.getCronDestackFlex())
            );
        }
        if (jobsConfiguration.getCronAutoRestack() != null && !jobsConfiguration.getCronAutoRestack().isEmpty()) {
            scheduler.schedule(
                "Lancement du restack automatique",
                autoRestackService::automaticReStack,
                Schedules.executeAt(jobsConfiguration.getCronAutoRestack())
            );
        }
    }
}
