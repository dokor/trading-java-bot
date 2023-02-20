package fr.lelouet.services.external.binance.utils;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.binance.connector.client.impl.SpotClientImpl;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lelouet.services.configuration.ConfigurationService;
import fr.lelouet.services.external.binance.config.bean.BinanceApiKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Gestionnaire du client principal de l'api Binance
 * Proposes les fonctions utilitaires pour réaliser les appels API
 */
@Singleton
public class BinanceGlobalProvider {

    /**
     * Documentations externes :
     *
     * Bible : https://binance-docs.github.io/apidocs/spot/en/#api-key-setup
     * Users can use the SPOT Testnet to practice SPOT trading : https://testnet.binance.vision/
     * Swagger API : https://binance.github.io/binance-api-swagger/
     * ErrorCode : https://binance-docs.github.io/apidocs/spot/en/#10xx-general-server-or-network-issues
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

    //Implements
    // SPOT
    // GET /api/v3/openOrders (HMAC SHA256) => Get all open orders on a symbol. Careful when accessing this with no symbol.GET /api/v3/openOrders (HMAC SHA256)
    // GET /api/v3/allOrders (HMAC SHA256) =>  Get all account orders; active, canceled, or filled.
    // GET /api/v3/account (HMAC SHA256) => Get current account information.
    public SpotClient getSpotClient() {
        return this.client;
    }

    public <T, K> T callBinanceApi(K client, String functionName, Class<T> returnType, LinkedHashMap<String, Object> stringObjectLinkedHashMap) {
        try {
            Method method = client.getClass().getMethod(functionName, String.class);
            String response = (String) method.invoke(client, stringObjectLinkedHashMap);
            // Vérifier si le type de retour est une liste ou non
            if (List.class.isAssignableFrom(returnType)) {
                JavaType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, returnType);
                return objectMapper.readValue(response, listType);
            } else {
                return objectMapper.readValue(response, returnType);
            }

        } catch (BinanceConnectorException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
