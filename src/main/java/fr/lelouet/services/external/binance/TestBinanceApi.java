package fr.lelouet.services.external.binance;

import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.internal.AutoRestackService;
import fr.lelouet.services.slack.SlackService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class de test pour préparer la mise en place des algos
 */
@Singleton
public class TestBinanceApi {

    private static final Logger logger = LoggerFactory.getLogger(TestBinanceApi.class);
    private final AutoRestackService autoRestackService;

    @Inject
    public TestBinanceApi(
        AutoRestackService autoRestackService
    ) {
        this.autoRestackService = autoRestackService;
    }

    @SneakyThrows
    public void mainTest() {
        logger.info("------------- Lancement des tests principaux -------------");
        autoRestackService.destackFlexibleStaking();
        autoRestackService.automatiqueReStack();
        logger.info("------------- Fin des tests principaux -------------");
    }


}
