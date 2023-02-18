package fr.lelouet.services.external.binance.wallet.bean;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record CoinsWalletInformations(
    List<CoinWallet> coinWalletList
//    Map<String, CoinWallet> coinWalletMap
) {

    public List<CoinWallet> getAllLegalMoney() {
        return this.coinWalletList().stream().filter(CoinWallet::isLegalMoney).toList();
    }

    public List<CoinWallet> getAllNotLegalMoney() {
        return this.coinWalletList().stream().filter(coinWallet -> !coinWallet.isLegalMoney()).toList();
    }

    public List<CoinWallet> getCoinsNotZero(){
        return this.coinWalletList().stream()
            .filter(coinWallet ->
                !Objects.equals(coinWallet.free(), "0")
                || !Objects.equals(coinWallet.freeze(), "0")
                || !Objects.equals(coinWallet.ipoable(), "0")
                || !Objects.equals(coinWallet.ipoing(), "0")
                || !Objects.equals(coinWallet.locked(), "0")
                || !Objects.equals(coinWallet.storage(), "0")
                || !Objects.equals(coinWallet.withdrawing(), "0")
        )
            .sorted(Comparator.comparing(CoinWallet::coin))
            .toList();
    }

    /**
     *  TODO : faire fonctions
     *  récupérer la liste des cryptos disponibles
     *  Calculer le montant moyen en € de chaque ligne
     *  Avoir une MAP par coin
     */

}
