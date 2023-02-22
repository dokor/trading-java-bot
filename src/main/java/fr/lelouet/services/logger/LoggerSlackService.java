package fr.lelouet.services.logger;

import fr.lelouet.services.internal.AutoRestackService;
import fr.lelouet.services.logger.enums.LogType;
import fr.lelouet.services.slack.SlackService;
import fr.lelouet.services.slack.bean.SlackMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class permetant de logger à la fois dans la console et dans un channel slack
 */
@Singleton
public class LoggerSlackService {

    private static final Logger logger = LoggerFactory.getLogger(LoggerSlackService.class);


    private final SlackService slackService;

    // TODO : a voir si ca a un vrai interet
    // TODO : Est ce que ca serait pas plus simple d'extends un logger directement
    @Inject
    public LoggerSlackService(
        SlackService slackService
    ) {
        this.slackService = slackService;
    }

    public void logMessage(LogType logType, String message, SlackMessageType slackMessageType) {
        this.log(logType, message, slackMessageType);
        this.sendSlack(logType, message, slackMessageType);
    }

    private void log(LogType logType, String message, SlackMessageType slackMessageType) {
        logger.info(message);
    }

    private void sendSlack(LogType logType, String message, SlackMessageType slackMessageType) {
        slackService.sendMessage(message, slackMessageType);
    }
}
