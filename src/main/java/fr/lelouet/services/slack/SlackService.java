package fr.lelouet.services.slack;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.slack.bean.SlackConfiguration;
import fr.lelouet.services.slack.enums.SlackMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * Service de gestion de l'envoie de messages Slack
 */
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

    /**
     * Méthode public permetant d'envoyer un message sur un channel Slack
     * Ex :
     *  SlackService.sendMessage("mon message", SlackMessageType.INFORMATIF);
     */
    public void sendMessage(String message, SlackMessageType slackMessageType) {
        String messageComputed = this.computeMessage(message, slackMessageType);
        this.processRequest(messageComputed, slackMessageType);
    }

    /**
     * Compute le payload du message Slack et envoie le message vers le Webhook Slack
     */
    private void processRequest(String message, SlackMessageType slackMessageType) {
        Payload payload = Payload.builder()
            .channel(slackMessageType.getChannel())
            .username(slackMessageType.getUsername())
            .iconEmoji(slackMessageType.getIcon())
            .text(message)
            .build();
        try {
            WebhookResponse webhookResponse = Slack.getInstance().send(slackConfiguration.url(), payload);
            logger.debug("Slack Response [{}] : {}", webhookResponse.getCode(), webhookResponse.getMessage());
        } catch (IOException e) {
            logger.error("Slack Error : ", e);
        }
    }

    /**
     * Compute du message à envoyer
     */
    private String computeMessage(String message, SlackMessageType slackMessageType) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (slackMessageType) {
            case LANCEMENT -> stringBuilder.append("LANCEMENT ");
            case INFORMATIF -> stringBuilder.append("INFORMATIF ");
            default -> stringBuilder.append("");
        }
        stringBuilder.append(message);
        return stringBuilder.toString();
    }

}
