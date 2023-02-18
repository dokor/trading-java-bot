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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Singleton
public class BinanceWalletClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceWalletClientApi.class);

    private final ObjectMapper objectMapper;

    private final Wallet clientWallet;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceWalletClientApi(
        ObjectMapper objectMapper,
        // todo trouver comment passer directement le spotWallet plutot que de réinjecter toute la classe
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.objectMapper = objectMapper;
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.clientWallet = binanceGlobalProvider.getSpotClient().createWallet();
    }

    @SneakyThrows
    public List<CoinWallet> getAllCoinsOfConnectedUser() {
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put("timestamp", Instant.now());
        try {
            String response = clientWallet.coinInfo(stringObjectLinkedHashMap);
            if (response != null) {
//                logger.debug("Response from Binance API : {}", response);
                return Arrays.stream(objectMapper.readValue(response, CoinWallet[].class)).toList();
            }
        } catch (BinanceConnectorException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }
        return null;
//        return this.executeBinanceWalletRequest(null, stringObjectLinkedHashMap, CoinsWalletInformations.class);
    }


    // Todo Remplacer le path par la fonction du client à exécuter
    @SneakyThrows
    private <T> BinanceResponse<T> executeBinanceWalletRequest(String path, LinkedHashMap<String, Object> bodyParameter, Class<T> responseClass) {
        try {
            String response = clientWallet.coinInfo(bodyParameter);
            if (response != null) {
                logger.debug("Response from Binance API : {}", response);
                return read(Objects.requireNonNull(response), responseClass);

            }
        } catch (BinanceConnectorException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }
        return null;
    }

    @SneakyThrows
    private <T> BinanceResponse<T> read(String json, Class<T> contentClass) {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(BinanceResponse.class, contentClass);
        return objectMapper.readValue(json, type);
    }

    @SneakyThrows
    private <T> BinanceResponse<List<T>> readArray(String json, Class<T> contentClass) {
        JavaType type = objectMapper.getTypeFactory()
            .constructParametricType(
                BinanceResponse.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, contentClass)
            );
        return objectMapper.readValue(json, type);
    }

}
