package fr.lelouet.services.external.binance.wallet;

import com.binance.connector.client.impl.spot.Wallet;
import fr.lelouet.services.external.binance.config.enums.BinanceQueryParam;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import fr.lelouet.services.external.binance.wallet.bean.CoinWallet;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import fr.lelouet.services.external.binance.wallet.bean.TradingFee;
import fr.lelouet.services.external.binance.wallet.bean.TradingFeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.LinkedHashMap;

@Singleton
public class BinanceWalletClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceWalletClientApi.class);

    private final Wallet clientWallet;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceWalletClientApi(
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.clientWallet = binanceGlobalProvider.getSpotClient().createWallet();
    }

    /**
     * Récupération des différentes monnaies de l'utilisateur dans Spot
     */
    public CoinsWalletInformations coinInfo() {
        logger.debug("Tentative de récupération des différentes monnaies du user courant");
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        CoinWallet[] coinWalletList = binanceGlobalProvider.callBinanceApi(clientWallet, "coinInfo", CoinWallet[].class, stringObjectLinkedHashMap);
        if (coinWalletList != null) {
            return CoinsWalletInformations.of(Arrays.stream(coinWalletList).toList());
        }
        return null;
    }

    public CoinWallet getCoinWallet(String asset) {
        logger.debug("Tentative de récupération du spot de [{}]", asset);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        stringObjectLinkedHashMap.put(BinanceQueryParam.ASSET.getValue(), asset);
        CoinWallet[] coinWallets = binanceGlobalProvider.callBinanceApi(clientWallet, "getUserAsset", CoinWallet[].class, stringObjectLinkedHashMap);
        if (coinWallets != null && coinWallets.length > 0) {
            return coinWallets[0];
        }
        return null;
    }

    public TradingFeeResponse getTradeFee(String asset) {
        logger.debug("Tentative de récupération des trades fee associé au compte");
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        if (asset != null) {
            stringObjectLinkedHashMap.put(BinanceQueryParam.ASSET.getValue(), asset);
        }
        TradingFee[] tradeFees = binanceGlobalProvider.callBinanceApi(clientWallet, "tradeFee", TradingFee[].class, stringObjectLinkedHashMap);
        if (tradeFees != null) {
            return new TradingFeeResponse(Arrays.stream(tradeFees).toList());
        }
        return null;
    }
}
