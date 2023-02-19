package fr.lelouet.services.external.binance.wallet.bean;

/**
 * Bean Binance à la récupération des coins spot de l'utilisateur
 */
public record CoinWallet(
    String coin,
    String name,
    boolean depositAllEnable,
    String free,
    String freeze,
    String ipoable,
    String ipoing,
    boolean isLegalMoney,
    String locked,
    Object networkList,
    String storage,
    String trading,
    String withdrawAllEnable,
    String withdrawing
) {

    public String toCloseString() {
        return "CoinWallet [" + this.coin + "] " +
            "=> free [" + this.free + "]"
            + " freeze [" + this.freeze + ']'
            + " locked [" + this.locked + ']'
            + " storage [" + this.storage + ']'
            + " withdrawing [" + this.withdrawing + ']';
    }

    private static Double convertStringToDouble(String ammount) {
        // TODO Vérifier la conversion avec les doubles + les virgules de l'infini
        return Double.valueOf(ammount);
    }

    public Double getTotalAmmount() {
        return convertStringToDouble(this.free) +
            convertStringToDouble(this.freeze) +
            convertStringToDouble(this.locked) +
            convertStringToDouble(this.storage) +
            convertStringToDouble(this.withdrawing);
    }
}

/**
 * MOCK exemple
 * {
 * "coin": "BTC",
 * "depositAllEnable": true,
 * "free": "0.08074558",
 * "freeze": "0.00000000",
 * "ipoable": "0.00000000",
 * "ipoing": "0.00000000",
 * "isLegalMoney": false,
 * "locked": "0.00000000",
 * "name": "Bitcoin",
 * "storage": "0.00000000",
 * "trading": true,
 * "withdrawAllEnable": true,
 * "withdrawing": "0.00000000"
 * "networkList": [
 * {
 * "addressRegex": "^(bnb1)[0-9a-z]{38}$",
 * "coin": "BTC",
 * "depositDesc": "Wallet Maintenance, Deposit Suspended", // shown only when "depositEnable" is false.
 * "depositEnable": false,
 * "isDefault": false,
 * "memoRegex": "^[0-9A-Za-z\\-_]{1,120}$",
 * "minConfirm": 1,  // min number for balance confirmation
 * "name": "BEP2",
 * "network": "BNB",
 * "resetAddressStatus": false,
 * "specialTips": "Both a MEMO and an Address are required to successfully deposit your BEP2-BTCB tokens to Binance.",
 * "unLockConfirm": 0,  // confirmation number for balance unlock
 * "withdrawDesc": "Wallet Maintenance, Withdrawal Suspended", // shown only when "withdrawEnable" is false.
 * "withdrawEnable": false,
 * "withdrawFee": "0.00000220",
 * "withdrawIntegerMultiple": "0.00000001",
 * "withdrawMax": "9999999999.99999999",
 * "withdrawMin": "0.00000440",
 * "sameAddress": true,  // If the coin needs to provide memo to withdraw
 * "estimatedArrivalTime": 25,
 * "busy": false
 * },
 * {
 * "addressRegex": "^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$|^(bc1)[0-9A-Za-z]{39,59}$",
 * "coin": "BTC",
 * "depositEnable": true,
 * "isDefault": true,
 * "memoRegex": "",
 * "minConfirm": 1,
 * "name": "BTC",
 * "network": "BTC",
 * "resetAddressStatus": false,
 * "specialTips": "",
 * "unLockConfirm": 2,
 * "withdrawEnable": true,
 * "withdrawFee": "0.00050000",
 * "withdrawIntegerMultiple": "0.00000001",
 * "withdrawMax": "750",
 * "withdrawMin": "0.00100000",
 * "sameAddress": false,
 * "estimatedArrivalTime": 25,
 * "busy": false
 * }
 * ],
 * <p>
 * }
 */