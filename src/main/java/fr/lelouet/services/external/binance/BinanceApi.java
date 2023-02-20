package fr.lelouet.services.external.binance;

import fr.lelouet.services.external.binance.config.enums.CryptoAsset;
import fr.lelouet.services.external.binance.market.BinanceMarketClientApi;
import fr.lelouet.services.external.binance.saving.BinanceSavingClientApi;
import fr.lelouet.services.external.binance.staking.BinanceStakingClientApi;
import fr.lelouet.services.external.binance.staking.bean.PersonalLeftQuota;
import fr.lelouet.services.external.binance.staking.bean.ProductResponse;
import fr.lelouet.services.external.binance.staking.bean.StakingPositions;
import fr.lelouet.services.external.binance.staking.bean.StakingProducts;
import fr.lelouet.services.external.binance.trade.BinanceTradeClientApi;
import fr.lelouet.services.external.binance.wallet.BinanceWalletClientApi;
import fr.lelouet.services.external.binance.wallet.bean.CoinsWalletInformations;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Service principal d'accés à l'api Binance
 * Implémente l'ensemble des clients Binance
 * Liste l'ensemble des appels disponibles pour contacter Binance
 * <p>
 * Cela doit etre le seul point d'entrée vers l'api binance
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

    // Wallet
    public CoinsWalletInformations getCoinsInformationsOfSpotWallet() {
        return binanceWalletClientApi.coinInfo();
    }

    // Market

    // Stacking

    /**
     * Liste les produits de staking disponibles pour l'ensemble des utilisateurs
     *
     * @param asset : Trigramme d'une crypto
     * @return : Liste les produits de staking
     */
    public StakingProducts getStakingProducts(String asset) {
        return binanceStakingClientApi.getStakingProducts(asset);
    }

    /**
     * Permet de souscrire à un produit de staking
     *
     * @param productId : Id du produit
     * @param amount    : Amount de l'asset à stacker
     * @return : Permet de vérifier le succés de l'opération et d'obtenir le positionId
     */
    public ProductResponse postStakingProducts(String productId, Double amount) {
        return binanceStakingClientApi.postStakingProducts(productId, amount);
    }

    /**
     * Pour un produit de staking donnée, permet de connaitre la consomation personnel du produit
     *
     * @param productId : Id du produit
     * @param asset     : Trigramme d'une crypto
     * @return
     */
    public StakingPositions getStakingPosition(String productId, String asset) {
        return binanceStakingClientApi.getStakingPosition(productId, asset);
    }

    /**
     * Permet de connaitre le quota restant sur un produit de staking donnée
     * @param productId : Id du produit sur lequel on veut vérifier le quota
     */
    public PersonalLeftQuota getPersonalLeftQuota(String productId) {
        return binanceStakingClientApi.getPersonalLeftQuota(productId);
    }

    // Trade

    // Saving

}
