package fr.lelouet.services.scheduler;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedules;
import com.coreoz.wisp.schedule.cron.CronSchedule;
import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.internal.AutoRestackService;
import fr.lelouet.services.scheduler.bean.JobsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;

/**
 * Classe de définition des taches schedulées.
 */
@Singleton
public class ScheduledJobs {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledJobs.class);

    private final Scheduler scheduler;
    private final AutoRestackService autoRestackService;
    private final JobsConfiguration jobsConfiguration;

    @Inject
    ScheduledJobs(Scheduler scheduler,
                  ConfigurationService configurationService,
                  AutoRestackService autoRestackService
    ) {
        this.scheduler = scheduler;
        this.jobsConfiguration = configurationService.getJobsConfiguration();
        this.autoRestackService = autoRestackService;
    }

    /**
     * Initialisation des taches schedulées
     */
    public void scheduleJobs() {
//        if (jobsConfiguration.getCronDestackFlex() != null && !jobsConfiguration.getCronDestackFlex().isEmpty()) {
//            scheduler.schedule(
//                "Lancement du destack flexible automatique",
//                autoRestackService::destackFlexibleStaking,
//                CronSchedule.parseUnixCron(jobsConfiguration.getCronDestackFlex())
//            );
//        }
//        if (jobsConfiguration.getCronAutoRestack() != null && !jobsConfiguration.getCronAutoRestack().isEmpty()) {
//            scheduler.schedule(
//                "Lancement du restack automatique",
//                autoRestackService::automatiqueReStack,
//                CronSchedule.parseUnixCron(jobsConfiguration.getCronAutoRestack())
//            );
//        }

    }
}
