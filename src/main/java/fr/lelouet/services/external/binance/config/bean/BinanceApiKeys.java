package fr.lelouet.services.external.binance.config.bean;


public record BinanceApiKeys(String publicKey, String secretKey) {
    public static BinanceApiKeys of(String aPublic, String secret) {
        return new BinanceApiKeys(aPublic, secret);
    }
}
