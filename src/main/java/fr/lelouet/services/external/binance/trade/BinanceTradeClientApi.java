package fr.lelouet.services.external.binance.trade;

import com.binance.connector.client.impl.spot.Savings;
import com.binance.connector.client.impl.spot.Trade;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BinanceTradeClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceTradeClientApi.class);

    private final Trade client;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceTradeClientApi(
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.client = binanceGlobalProvider.getSpotClient().createTrade();
    }

//    /**
//     * Récupération des différentes monnaies de l'utilisateur dans Spot
//     */
//    // todo : a retester
//    public CoinsWalletInformations coinInfo() {
//        logger.debug("Tentative de récupération des différentes monnaies du user courant");
//        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
//        stringObjectLinkedHashMap.put("timestamp", Instant.now());
//        CoinWallet[] coinWalletList = binanceGlobalProvider.callBinanceApi(clientWallet, "coinInfo", CoinWallet[].class, stringObjectLinkedHashMap);
//        return CoinsWalletInformations.of(Arrays.stream(coinWalletList).toList());
//    }


    // TODO implements
    // GET /api/v3/depth => Récupere l'orderBook
    // GET /api/v3/ticker/bookTicker => Best price/qty on the order book for a symbol or symbols.

}
