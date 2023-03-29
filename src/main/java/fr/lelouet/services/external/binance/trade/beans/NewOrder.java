package fr.lelouet.services.external.binance.trade.beans;

import fr.lelouet.services.external.binance.trade.enums.NewOrderResponseType;
import fr.lelouet.services.external.binance.trade.enums.OrderSide;
import fr.lelouet.services.external.binance.trade.enums.OrderType;
import fr.lelouet.services.external.binance.trade.enums.TimeInForce;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * FIXME : Ajouter en buy et sell :
 * STOP_LOSS	quantity, stopPrice or trailingDelta	This will execute a MARKET order when the conditions are met. (e.g. stopPrice is met or trailingDelta is activated)
 * STOP_LOSS_LIMIT	timeInForce, quantity, price, stopPrice or trailingDelta
 * TAKE_PROFIT	quantity, stopPrice or trailingDelta	This will execute a MARKET order when the conditions are met. (e.g. stopPrice is met or trailingDelta is activated)
 * TAKE_PROFIT_LIMIT	timeInForce, quantity, price, stopPrice or trailingDelta
 * LIMIT_MAKER	quantity, price	This is a LIMIT order that will be rejected if the order immediately matches and trades as a taker.
 * This is also known as a POST-ONLY order.
 *
 * Other info:
 * Any LIMIT or LIMIT_MAKER type order can be made an iceberg order by sending an icebergQty.
 * Any order with an icebergQty MUST have timeInForce set to GTC.
 * For STOP_LOSS, STOP_LOSS_LIMIT, TAKE_PROFIT_LIMIT and TAKE_PROFIT orders, trailingDelta can be combined with stopPrice.
 * MARKET orders using quoteOrderQty will not break LOT_SIZE filter rules; the order will execute a quantity that will have the notional value as close as possible to quoteOrderQty. Trigger order price rules against market price for both MARKET and LIMIT versions:
 * Price above market price: STOP_LOSS BUY, TAKE_PROFIT SELL
 * Price below market price: STOP_LOSS SELL, TAKE_PROFIT BUY
 */


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NewOrder {
    /**
     * Symbol to place the order on.
     */
    private String symbol;

    /**
     * Buy/Sell order side.
     */
    private OrderSide side;

    /**
     * Type of order.
     */
    private OrderType type;

    /**
     * Time in force to indicate how long will the order remain active.
     */
    private TimeInForce timeInForce;

    /**
     * Quantity.
     */
    private String quantity;

    /**
     * Quote quantity.
     */
    private String quoteOrderQty;

    /**
     * Price.
     */
    private String price;

    /**
     * A unique id for the order. Automatically generated if not sent.
     */
    private String newClientOrderId;

    /**
     * Used with stop orders.
     */
    private String stopPrice;

    /**
     * Used with stop orders.
     */
    private String stopLimitPrice;

    /**
     * Used with iceberg orders.
     */
    private String icebergQty;

    /**
     * Set the response JSON. ACK, RESULT, or FULL; default: RESULT.
     */
    private NewOrderResponseType newOrderRespType;

    /**
     * Receiving window.
     */
    private Long recvWindow;

    /**
     * Order timestamp.
     */
    private long timestamp;

    /**
     * Creates a new order with all required parameters plus price, which is optional for MARKET orders.
     */
    public NewOrder(String symbol, OrderSide side, OrderType type, TimeInForce timeInForce, String quantity, String price) {
        this(symbol, side, type, timeInForce, quantity);
        this.price = price;
    }

    /**
     * Places a MARKET buy order for the given <code>quantity</code>.
     *
     * @return a new order which is pre-configured with MARKET as the order type and BUY as the order side.
     */
    public static NewOrder marketBuy(String symbol, String quantity) {
        return new NewOrder(symbol, OrderSide.BUY, OrderType.MARKET, null, quantity);
    }

    /**
     * Places a MARKET sell order for the given <code>quantity</code>.
     *
     * @return a new order which is pre-configured with MARKET as the order type and SELL as the order side.
     */
    public static NewOrder marketSell(String symbol, String quantity) {
        return new NewOrder(symbol, OrderSide.SELL, OrderType.MARKET, null, quantity);
    }

    /**
     * Places a LIMIT buy order for the given <code>quantity</code> and <code>price</code>.
     *
     * @return a new order which is pre-configured with LIMIT as the order type and BUY as the order side.
     */
    public static NewOrder limitBuy(String symbol, TimeInForce timeInForce, String quantity, String price) {
        return new NewOrder(symbol, OrderSide.BUY, OrderType.LIMIT, timeInForce, quantity, price);
    }

    /**
     * Places a LIMIT sell order for the given <code>quantity</code> and <code>price</code>.
     *
     * @return a new order which is pre-configured with LIMIT as the order type and SELL as the order side.
     */
    public static NewOrder limitSell(String symbol, TimeInForce timeInForce, String quantity, String price) {
        return new NewOrder(symbol, OrderSide.SELL, OrderType.LIMIT, timeInForce, quantity, price);
    }


    /**
     * Creates a new order with all required parameters.
     */
    public NewOrder(String symbol, OrderSide side, OrderType type, TimeInForce timeInForce, String quantity) {
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.timeInForce = timeInForce;
        this.quantity = quantity;
        this.newOrderRespType = NewOrderResponseType.RESULT;
        this.timestamp = System.currentTimeMillis();
        this.recvWindow = 60_000L;
    }
}
