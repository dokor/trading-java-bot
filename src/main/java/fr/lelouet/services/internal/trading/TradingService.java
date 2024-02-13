package fr.lelouet.services.internal.trading;

import fr.lelouet.services.external.binance.BinanceApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TradingService {

    private final BinanceApi binanceApi;

    @Inject
    public TradingService(
        BinanceApi binanceApi
    ) {
        this.binanceApi = binanceApi;
    }

    /**
     * TODO : Equilibrer les comtpes FIAT
     */
    public void todo() {
        // Récupérer le prix courant du symbol BUSD/USDT
        // Trouver une Pair Crypto qui a un prix en BUSD plus faible que celui en USDT
        // Prendre en compte les Fee et la liquidité
        // Acheter en BUSD, revendre en USDT + Mettre un limit au prix + fee

        // Faire la meme chose avec d'autres FIAT
    }

}
