package fr.lelouet.services.external.binance.wallet;

import com.binance.connector.client.impl.spot.Wallet;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import fr.lelouet.services.external.binance.wallet.bean.CoinWallet;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
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

    // TODO implements
    // GET /sapi/v1/accountSnapshot   => Daily Account Snapshot
    // GET /sapi/v1/asset/tradeFee   =>  Fetch trade fee
    // POST /sapi/v1/asset/transfer (HMAC SHA256) =>  You need to enable Permits Universal Transfer option for the API Key which requests this endpoint.
    // GET /sapi/v1/asset/transfer (HMAC SHA256) => Get les transferts de compte actuels
    // POST /sapi/v1/asset/get-funding-asset (HMAC SHA256) => Récuperer le solde d'un asset dans spot, utile que pour requeter les comptes parallaleles (CB etc)
    // POST /sapi/v3/asset/getUserAsset => Get user assets, just for positive data. (plus simple que l'api CoinInfo

}
