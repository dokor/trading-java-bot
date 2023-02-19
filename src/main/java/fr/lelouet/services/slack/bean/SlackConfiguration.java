package fr.lelouet.services.slack.bean;

public record SlackConfiguration(String url, String token) {
    public static SlackConfiguration of(String url, String token) {
        return new SlackConfiguration(url, token);
    }
}
