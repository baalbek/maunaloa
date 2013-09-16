package maunaloa.events;

import oahu.financial.StockPrice;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 9/16/13
 * Time: 11:46 PM
 */
public class StockPriceAssignedEvent {
    private final StockPrice stockPrice;

    public StockPriceAssignedEvent(StockPrice stockPrice) {
        this.stockPrice = stockPrice;
    }

    public StockPrice getStockPrice() {
        return stockPrice;
    }
}
