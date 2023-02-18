package fr.lelouet.services.configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.typesafe.config.Config;
import fr.lelouet.services.external.binance.bean.BinanceApiKeys;

@Singleton
public class ConfigurationService {

    private final Config config;

    private final static String API_BINANCE_KEY = "api.binance.key";

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

    public BinanceApiKeys getBinanceKeys() {
        Config configBinance = config.getConfig(API_BINANCE_KEY);
        return BinanceApiKeys.of(
            configBinance.getString("public"),
            configBinance.getString("secret")
        );
    }
}
