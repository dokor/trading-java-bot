package fr.lelouet.services.external.binance.market.beans;

import java.util.List;


public record OrderBook(Long lastUpdateId, List<OrderBookEntry> bids, List<OrderBookEntry> asks) {
}
