package fr.lelouet.services.external.binance.staking;

import com.binance.connector.client.impl.spot.Staking;
import fr.lelouet.services.external.binance.config.enums.BinanceQueryParam;
import fr.lelouet.services.external.binance.staking.bean.PersonalLeftQuota;
import fr.lelouet.services.external.binance.staking.bean.ProductResponse;
import fr.lelouet.services.external.binance.staking.bean.ProjectStaking;
import fr.lelouet.services.external.binance.staking.bean.StakingPosition;
import fr.lelouet.services.external.binance.staking.bean.StakingPositions;
import fr.lelouet.services.external.binance.staking.bean.StakingProducts;
import fr.lelouet.services.external.binance.staking.enums.ProductType;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Client d'api interne du module Staking de l'api Binance
 */
@Singleton
public class BinanceStakingClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceStakingClientApi.class);

    private final Staking client;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceStakingClientApi(
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.client = binanceGlobalProvider.getSpotClient().createStaking();
    }

    public StakingProducts getStakingProducts(String asset) {
        logger.debug("Tentative de récupération des staking products [{}] du user courant", asset);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.SIZE.getValue(), 100);
        stringObjectLinkedHashMap.put(BinanceQueryParam.ASSET.getValue(), asset);
        stringObjectLinkedHashMap.put(BinanceQueryParam.PRODUCT.getValue(), ProductType.STAKING.name());
        ProjectStaking[] projectStakings = binanceGlobalProvider.callBinanceApi(client, "productList", ProjectStaking[].class, stringObjectLinkedHashMap);
        if (projectStakings != null) {
            return StakingProducts.of(Arrays.stream(projectStakings).toList());
        }
        return null;
    }

    public ProductResponse postStakingProducts(String productId, Double amount) {
        logger.debug("Tentative d'achat du stacking [{}] pour un montant de [{}]", productId, amount);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.PRODUCT.getValue(), ProductType.STAKING.name());
        stringObjectLinkedHashMap.put(BinanceQueryParam.PRODUCT_ID.getValue(), productId);
        stringObjectLinkedHashMap.put(BinanceQueryParam.AMOUNT.getValue(), amount);
        return binanceGlobalProvider.callBinanceApi(client, "purchase", ProductResponse.class, stringObjectLinkedHashMap);
    }

    public StakingPositions getStakingPosition(String productId, String asset) {
        logger.debug("Tentative de récupération du stacking [{}] de [{}]", productId, asset);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.PRODUCT.getValue(), ProductType.STAKING.name());
        stringObjectLinkedHashMap.put(BinanceQueryParam.PRODUCT_ID.getValue(), productId);
        stringObjectLinkedHashMap.put(BinanceQueryParam.ASSET.getValue(), asset);
        stringObjectLinkedHashMap.put(BinanceQueryParam.SIZE.getValue(), 100);
        StakingPosition[] positions = binanceGlobalProvider.callBinanceApi(client, "getPosition", StakingPosition[].class, stringObjectLinkedHashMap);
        if (positions != null) {
            return StakingPositions.of(Arrays.stream(positions).toList());
        }
        return null;
    }

    public PersonalLeftQuota getPersonalLeftQuota(String productId) {
        logger.debug("Tentative de récupération du quota personnel restant sur le stacking [{}]", productId);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.PRODUCT.getValue(), ProductType.STAKING.name());
        stringObjectLinkedHashMap.put(BinanceQueryParam.PRODUCT_ID.getValue(), productId);
        return binanceGlobalProvider.callBinanceApi(client, "personalLeftQuota", PersonalLeftQuota.class, stringObjectLinkedHashMap);
    }

}
