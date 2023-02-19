package fr.lelouet.services.external.binance;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Wallet;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.external.binance.config.bean.BinanceApiKeys;
import fr.lelouet.services.external.binance.config.bean.BinanceResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Gestionnaire des différents clients d'api Binance
 */
@Singleton
public class BinanceGlobalProvider {

    /**
     * Documentation externe :
     * Bible : https://binance-docs.github.io/apidocs/spot/en/#api-key-setup
     * Users can use the SPOT Testnet to practice SPOT trading : https://testnet.binance.vision/
     */

    private static final Logger logger = LoggerFactory.getLogger(BinanceGlobalProvider.class);
    private final SpotClient client;
    private final BinanceApiKeys binanceApiKeys;
    private final ObjectMapper objectMapper;

    @Inject
    public BinanceGlobalProvider(
        ObjectMapper objectMapper,
        ConfigurationService configurationService
    ) {
        // Récupération des clés d'api binance depuis la configuration projejt
        this.binanceApiKeys = configurationService.getBinanceKeys();
        this.objectMapper = objectMapper;
        // Création du client principal
        this.client = new SpotClientImpl(binanceApiKeys.publicKey(), binanceApiKeys.secretKey());
        logger.info("Client d'API Binance global crée");
        // todo : regarder l'histoire de changement de base endpoint pour gagner en perf
    }

    public void initClient() {
//        logger.info("Initialisation du client Binance Staking");
//        Staking clientStaking = client.createStaking();
//
//        logger.info("Initialisation du client Binance Market");
//        Market clientMarket = client.createMarket();
//
//        logger.info("Initialisation du client Binance Savings");
//        Savings clientSavings = client.createSavings();
//
//        logger.info("Initialisation du client Binance UserData");
//        UserData clientUserData = client.createUserData();
    }

    //Implements
    // MARKET
    // GET /api/v3/depth => Récupere l'orderBook
    // GET /api/v3/ticker/bookTicker => Best price/qty on the order book for a symbol or symbols.

    // SPot
    // GET /api/v3/openOrders (HMAC SHA256) => Get all open orders on a symbol. Careful when accessing this with no symbol.GET /api/v3/openOrders (HMAC SHA256)
    // GET /api/v3/allOrders (HMAC SHA256) =>  Get all account orders; active, canceled, or filled.
    // GET /api/v3/account (HMAC SHA256) => Get current account information.

    // SAVINGS
    // STAKINGS

    public SpotClient getSpotClient() {
        return this.client;
    }

    @SneakyThrows
    public <T> BinanceResponse<T> read(String json, Class<T> contentClass) {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(BinanceResponse.class, contentClass);
        return objectMapper.readValue(json, type);
    }

    @SneakyThrows
    public  <T> List<T> readArray(String json, Class<T> contentClass) {
        JavaType type = objectMapper.getTypeFactory()
            .constructParametricType(
                BinanceResponse.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, contentClass)
            );
        return objectMapper.readValue(json, type);
    }

    /**
     * Info trade:
     * "base asset" refers to the asset that is the "quantity" of a symbol. For the symbol BTCUSDT, BTC would be the base asset.
     * "quote asset" refers to the asset that is the "price" of a symbol. For the symbol BTCUSDT, USDT would be the quote asset.
     */

    /**
     * Exemple de gestion d'erreur :
     * try {
     *       String result = client.createTrade().newOrder(parameters);
     *       logger.info(result);
     *     } catch (BinanceConnectorException e) {
     *       logger.error("fullErrMessage: {}", e.getMessage(), e);
     *     } catch (BinanceClientException e) {
     *       logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
     *       e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
     *     }
     */


    /**
     * Base endpoint without auth for market info :
     * The base endpoint https://data.binance.com can be used to access the following API endpoints that have NONE as security type:
     * GET /api/v3/aggTrades
     * GET /api/v3/avgPrice
     * GET /api/v3/depth
     * GET /api/v3/exchangeInfo
     * GET /api/v3/klines
     * GET /api/v3/ping
     * GET /api/v3/ticker
     * GET /api/v3/ticker/24hr
     * GET /api/v3/ticker/bookTicker
     * GET /api/v3/ticker/price
     * GET /api/v3/time
     * GET /api/v3/trades
     * GET /api/v3/uiKlines
     */


    /**
     * HTTP Return Codes
     * HTTP 4XX return codes are used for malformed requests; the issue is on the sender's side.
     * HTTP 403 return code is used when the WAF Limit (Web Application Firewall) has been violated.
     * HTTP 409 return code is used when a cancelReplace order partially succeeds. (e.g. if the cancellation of the order fails but the new order placement succeeds.)
     * HTTP 429 return code is used when breaking a request rate limit.
     * HTTP 418 return code is used when an IP has been auto-banned for continuing to send requests after receiving 429 codes.
     * HTTP 5XX return codes are used for internal errors; the issue is on Binance's side. It is important to NOT treat this as a failure operation; the execution status is UNKNOWN and could have been a success.
     *
     * Autres : https://binance-docs.github.io/apidocs/spot/en/#10xx-general-server-or-network-issues
     */
}
