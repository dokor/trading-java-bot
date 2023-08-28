package fr.lelouet.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import fr.lelouet.guice.TestModule;
import fr.lelouet.services.external.binance.BinanceApi;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;
import fr.lelouet.services.external.binance.wallet.bean.TradingFee;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceTestRunner.class)
@GuiceModules(TestModule.class)
public class BinanceApiTest {

    @Inject
    BinanceApi binanceApi;

    @Test
    public void getCoinsInformationsOfSpotWallet() {
        CoinsWalletInformations coinsInformationsOfSpotWallet = binanceApi.getCoinsInformationsOfSpotWallet();
        assertThat(coinsInformationsOfSpotWallet).isNotNull();
    }

    @Test
    public void getTradingFee() {
        Map<String, TradingFee> stringTradingFeeMap = binanceApi.getTradingFee(null).transform();
        TradingFee bitcoin = stringTradingFeeMap.get("BTCBUSD");
        assertThat(bitcoin).isNotNull();
    }
}
