package fr.lelouet.services.external.binance.stacking;

import com.binance.connector.client.impl.spot.Staking;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BinanceStackingClientApi {

    private static final Logger logger = LoggerFactory.getLogger(BinanceStackingClientApi.class);

    private final Staking client;
    private final BinanceGlobalProvider binanceGlobalProvider;

    @Inject
    public BinanceStackingClientApi(
        BinanceGlobalProvider binanceGlobalProvider
    ) {
        this.binanceGlobalProvider = binanceGlobalProvider;
        this.client = binanceGlobalProvider.getSpotClient().createStaking();
    }

}
