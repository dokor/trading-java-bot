package fr.lelouet.services.external.binance.saving;

import com.binance.connector.client.enums.HttpMethod;
import com.binance.connector.client.impl.spot.Savings;
import com.binance.connector.client.utils.ParameterChecker;
import fr.lelouet.services.external.binance.config.bean.BinanceResponse;
import fr.lelouet.services.external.binance.config.enums.BinanceQueryParam;
import fr.lelouet.services.external.binance.saving.bean.FlexiblePosition;
import fr.lelouet.services.external.binance.saving.enums.RedeemType;
import fr.lelouet.services.external.binance.staking.bean.ProjectStaking;
import fr.lelouet.services.external.binance.staking.bean.StakingProducts;
import fr.lelouet.services.external.binance.staking.enums.ProductType;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Singleton
public class BinanceSavingClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceSavingClientApi.class);

    private final Savings client;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceSavingClientApi(
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.client = binanceGlobalProvider.getSpotClient().createSavings();
    }

    /**
     * Permet de récupérer les fonds investis dans un produit de staking flexible.
     * Il est possible de récupérer en FAST (= instantanné) ou en NORMAL (= 02h du matin de j+1)
     */
    public void redeemFlexibleProduct(String productId, Double amount, RedeemType redeemType) {
        logger.debug("Tentative de redeem du staking [{}] d'un montant de [{}] en [{}]", productId, amount, redeemType.name());
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.TYPE.getValue(), redeemType.name());
        stringObjectLinkedHashMap.put(BinanceQueryParam.PRODUCT_ID.getValue(), productId);
        stringObjectLinkedHashMap.put(BinanceQueryParam.AMOUNT.getValue(), amount);
        binanceGlobalProvider.callBinanceApi(client, "redeemFlexibleProduct", String.class, stringObjectLinkedHashMap);
    }

    /**
     * Récupére l'ensemble des postions flexible disponible pour un asset précis
     */
    public List<FlexiblePosition> flexibleProductPosition(String asset) {
        logger.debug("Tentative de get du staking de [{}]", asset);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        if (asset != null) {
            stringObjectLinkedHashMap.put(BinanceQueryParam.ASSET.getValue(), asset);
        }
        FlexiblePosition[] flexiblePositions = binanceGlobalProvider.callBinanceApi(client, "flexibleProductPosition", FlexiblePosition[].class, stringObjectLinkedHashMap);
        if (flexiblePositions != null) {
            return Arrays.stream(flexiblePositions).toList();
        }
        return null;
    }

    //Get Fixed and Activity Project List
//    public String projectList(LinkedHashMap<String, Object> parameters) {
//        ParameterChecker.checkParameter(parameters, "type", String.class);
//        return requestHandler.sendSignedRequest(baseUrl, ACTIVITY_PROJECT, parameters, HttpMethod.GET, showLimitUsage);
//    }

    // Get Fixed/Activity Project Position
//    public String projectPosition(LinkedHashMap<String, Object> parameters) {
//        return requestHandler.sendSignedRequest(baseUrl, PROJECT_POSITION, parameters, HttpMethod.GET, showLimitUsage);
//    }

    // TODO implements
    // GET /api/v3/depth => Récupere l'orderBook
    // GET /api/v3/ticker/bookTicker => Best price/qty on the order book for a symbol or symbols.

}
