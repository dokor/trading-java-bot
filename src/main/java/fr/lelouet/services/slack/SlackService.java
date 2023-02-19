package fr.lelouet.services.slack;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.slack.bean.SlackConfiguration;
import fr.lelouet.services.slack.bean.SlackMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class SlackService {

    private static final Logger logger = LoggerFactory.getLogger(SlackService.class);

    private final SlackConfiguration slackConfiguration;

    @Inject
    public SlackService(
        ConfigurationService configurationService
    ) {
        this.slackConfiguration = configurationService.getSlackConfig();
    }

    public void sendMessage(String message, SlackMessageType slackMessageType) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (slackMessageType) {
            case LANCEMENT -> stringBuilder.append("LANCEMENT " + message);
            case INFORMATIF -> stringBuilder.append("INFORMATIF " + message);
            default -> stringBuilder.append(message);
        }
        processRequest(message);
    }

    private void processRequest(String message) {
        Payload payload = Payload.builder()
            .channel("#trading-bot-antoine")
            .username("Trading App Binance")
            .iconEmoji(":rocket:")
            .text(message)
            .build();

        try {
            WebhookResponse webhookResponse = Slack.getInstance().send(slackConfiguration.url(), payload);
            logger.debug("Slack Response [{}] : {}", webhookResponse.getCode(), webhookResponse.getMessage());
        } catch (IOException e) {
            logger.error("Slack Error : ", e);
        }
    }

}
