package fr.lelouet.services.external.binance.wallet;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.binance.connector.client.impl.spot.Wallet;
import com.binance.connector.client.utils.JSONParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import fr.lelouet.services.external.binance.BinanceGlobalProvider;
import fr.lelouet.services.external.binance.config.bean.BinanceResponse;
import fr.lelouet.services.external.binance.wallet.bean.CoinWallet;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Singleton
public class BinanceWalletClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceWalletClientApi.class);

    private final ObjectMapper objectMapper;

    private final Wallet clientWallet;
    private final BinanceGlobalProvider binanceGlobalProvider;

    // TODO faire un snapshot journalier = Scheduler
    // Daily Account Snapshot GET /sapi/v1/accountSnapshot

    @Inject
    public BinanceWalletClientApi(
        ObjectMapper objectMapper,
        // todo trouver comment passer directement le spotWallet en global app (ou package) plutot que de réinjecter toute la classe
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.objectMapper = objectMapper;
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.clientWallet = binanceGlobalProvider.getSpotClient().createWallet();
    }


    /**
     * Récupération des différentes monnaies de l'utilisateur dans Spot
     */
    @SneakyThrows
    public List<CoinWallet> getAllCoinsOfConnectedUser() {
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put("timestamp", Instant.now());
        try {
            String response = clientWallet.coinInfo(stringObjectLinkedHashMap);
            if (response != null) {
                return Arrays.stream(objectMapper.readValue(response, CoinWallet[].class)).toList();
            }
        } catch (BinanceConnectorException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }
        return Collections.emptyList();
    }

    // TODO implements
    // GET /sapi/v1/asset/tradeFee   =>  Fetch trade fee
    // POST /sapi/v1/asset/transfer (HMAC SHA256) =>  You need to enable Permits Universal Transfer option for the API Key which requests this endpoint.
    // GET /sapi/v1/asset/transfer (HMAC SHA256) => Get les transferts de compte actuels
    // POST /sapi/v1/asset/get-funding-asset (HMAC SHA256) => Récuperer le solde d'un asset dans spot
    // POST /sapi/v3/asset/getUserAsset => Get user assets, just for positive data. (plus simple que l'api CoinInfo


    // Todo Remplacer le path par la fonction du client à exécuter
    @SneakyThrows
    private <T> BinanceResponse<T> executeBinanceWalletRequest(String path, LinkedHashMap<String, Object> bodyParameter, Class<T> responseClass) {
        try {
            String response = clientWallet.coinInfo(bodyParameter);
            if (response != null) {
                logger.debug("Response from Binance API : {}", response);
                return binanceGlobalProvider.read(Objects.requireNonNull(response), responseClass);
            }
        } catch (BinanceConnectorException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }
        return null;
    }

}
