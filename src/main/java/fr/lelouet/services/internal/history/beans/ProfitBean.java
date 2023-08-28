package fr.lelouet.services.internal.history.beans;

import fr.lelouet.services.external.binance.trade.beans.PastOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfitBean {
    private String symbol;
    private ArrayList<PastOrder> buys = new ArrayList<>();
    private ArrayList<PastOrder> sells = new ArrayList<>();

    public void addBuy(PastOrder pastOrder) {
        if (buys == null) {
            buys = new ArrayList<>();
        }
        buys.add(pastOrder);
    }

    public void addSell(PastOrder pastOrder) {
        if (sells == null) {
            sells = new ArrayList<>();
        }
        sells.add(pastOrder);
    }


    public double totalBuyAmount() {
        if (buys == null) return 0.0;
        return buys.stream()
            .mapToDouble(pastOrder -> pastOrder.executedQty() * pastOrder.price())
            .sum();
    }

    public double totalSellAmount() {
        if (sells == null) return 0.0;
        return sells.stream()
            .mapToDouble(pastOrder -> pastOrder.executedQty() * pastOrder.price())
            .sum();
    }

    public double averageBuyPrice() {
        if (buys == null) return 0.0;
        double totalQty = buys.stream()
            .mapToDouble(PastOrder::executedQty)
            .sum();
        if (totalQty == 0) {
            return 0; // to avoid division by zero
        }
        return buys.stream()
            .mapToDouble(pastOrder -> (pastOrder.executedQty() * pastOrder.price()) / totalQty)
            .sum();
    }

    public double averageSellPrice() {
        if (sells == null) return 0.0;
        double totalQty = sells.stream()
            .mapToDouble(PastOrder::executedQty)
            .sum();
        if (totalQty == 0) {
            return 0; // to avoid division by zero
        }
        return sells.stream()
            .mapToDouble(pastOrder -> (pastOrder.executedQty() * pastOrder.price()) / totalQty)
            .sum();
    }

    public double netProfitOrLoss() {
        return totalSellAmount() - totalBuyAmount();
    }

    public double getCurrentBalance() {
        double totalBuyQty = 0.0;
        double totalSellQty = 0.0;

        if (buys != null) {
            totalBuyQty = buys.stream()
                .mapToDouble(PastOrder::executedQty)
                .sum();
        }
        if (sells != null) {
            totalSellQty = sells.stream()
                .mapToDouble(PastOrder::executedQty)
                .sum();

        }
        return totalBuyQty - totalSellQty;
    }
}
