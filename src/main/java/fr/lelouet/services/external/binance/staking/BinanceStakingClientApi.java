package fr.lelouet.services.external.binance.staking;

import com.binance.connector.client.impl.spot.Staking;
import fr.lelouet.services.external.binance.utils.BinanceGlobalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

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

}
