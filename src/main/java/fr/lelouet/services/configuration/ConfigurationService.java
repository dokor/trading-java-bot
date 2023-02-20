package fr.lelouet.services.configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.typesafe.config.Config;
import fr.lelouet.services.external.binance.config.bean.BinanceApiKeys;
import fr.lelouet.services.scheduler.bean.JobsConfiguration;
import fr.lelouet.services.slack.bean.SlackConfiguration;

import java.util.List;

@Singleton
public class ConfigurationService {

    private final Config config;

    private static final String API_BINANCE_KEY = "api.binance.key";
    private static final String API_SLACK = "api.slack";
    private static final String JOBS_CRON = "jobs.cron";

    @Inject
    public ConfigurationService(Config config) {
        this.config = config;
    }

    public String swaggerAccessUsername() {
        return config.getString("swagger.access.username");
    }

    public String swaggerAccessPassword() {
        return config.getString("swagger.access.password");
    }

    public SlackConfiguration getSlackConfig() {
        Config configSlack = config.getConfig(API_SLACK);
        return SlackConfiguration.of(
            configSlack.getString("url"),
            configSlack.getString("token")
        );
    }

    public BinanceApiKeys getBinanceKeys() {
        Config configBinance = config.getConfig(API_BINANCE_KEY);
        return BinanceApiKeys.of(
            configBinance.getString("public"),
            configBinance.getString("secret")
        );
    }

    public List<String> ignoreAutoStakingCryptoList() {
        return config.getStringList("algotithm.staking.crypto.list");
    }

    public JobsConfiguration getJobsConfiguration() {
        Config configBinance = config.getConfig(JOBS_CRON);
        return JobsConfiguration.of(
            configBinance.getString("auto-restack"),
            configBinance.getString("destack-flexible")
        );
    }
}
