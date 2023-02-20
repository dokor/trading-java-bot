package fr.lelouet.services.external.binance;

import fr.lelouet.services.external.binance.market.BinanceMarketClientApi;
import fr.lelouet.services.external.binance.saving.BinanceSavingClientApi;
import fr.lelouet.services.external.binance.staking.BinanceStakingClientApi;
import fr.lelouet.services.external.binance.trade.BinanceTradeClientApi;
import fr.lelouet.services.external.binance.wallet.BinanceWalletClientApi;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Service principal d'accés à l'api Binance
 * Implémente l'ensemble des clients Binance
 * Liste l'ensemble des appels disponibles pour contacter Binance
 */
@Singleton
public class BinanceApi {

    private final BinanceWalletClientApi binanceWalletClientApi;
    private final BinanceMarketClientApi binanceMarketClientApi;
    private final BinanceStakingClientApi binanceStakingClientApi;
    private final BinanceTradeClientApi binanceTradeClientApi;
    private final BinanceSavingClientApi binanceSavingClientApi;

    @Inject
    public BinanceApi(
        BinanceWalletClientApi binanceWalletClientApi,
        BinanceMarketClientApi binanceMarketClientApi,
        BinanceStakingClientApi binanceStakingClientApi,
        BinanceTradeClientApi binanceTradeClientApi,
        BinanceSavingClientApi binanceSavingClientApi
    ) {
        this.binanceWalletClientApi = binanceWalletClientApi;
        this.binanceMarketClientApi = binanceMarketClientApi;
        this.binanceStakingClientApi = binanceStakingClientApi;
        this.binanceTradeClientApi = binanceTradeClientApi;
        this.binanceSavingClientApi = binanceSavingClientApi;
    }

    public CoinsWalletInformations getCoinsInformationsOfSpotWallet(){
        return binanceWalletClientApi.coinInfo();
    }
}
