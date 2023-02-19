package fr.lelouet.services.external.binance.config.bean;


public record BinanceApiKeys(String publicKey, String secretKey) {
    public static BinanceApiKeys of(String publicKey, String secretKey) {
        return new BinanceApiKeys(publicKey, secretKey);
    }
}
