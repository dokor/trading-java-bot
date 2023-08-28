package fr.lelouet.services.external.binance.trade.beans;

import fr.lelouet.services.external.binance.trade.enums.OrderSide;
import fr.lelouet.services.external.binance.trade.enums.OrderType;
import fr.lelouet.services.external.binance.trade.enums.TimeInForce;

public record PastOrder(
    String symbol, //	BTCUSDT
    Long orderId, //	1055503580
    Integer orderListId, //	-1
    String clientOrderId, //	web_3f8123d26055459db70c1388c08b8ea8
    Double price, //	0.00000000
    Double origQty, //	0.03676700
    Double executedQty, //	0.03676700
    Double cummulativeQuoteQty, //	317.87755491
    String status, //	FILLED //todo : enum
    TimeInForce timeInForce, //	GTC
    OrderType type, //	MARKET
    OrderSide side, //	SELL
    Double stopPrice, //	0.00000000
    Double icebergQty, //	0.00000000
    Long time, //	1579516495231
    Long updateTime, //	1579516495231
    Boolean isWorking, //	true
    Long workingTime, //	1579516495231
    Double origQuoteOrderQty, //	0.00000000
    String selfTradePreventionMode //	NONE //todo : enum
) {
}
