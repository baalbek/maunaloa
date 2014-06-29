package maunaloa.repository;

import oahu.financial.Stock;
import oahu.financial.StockPrice;

import java.util.Collection;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by rcs on 4/14/14.
 */
public interface StockRepository {
    List<Stock> getStocks();
    Collection<StockPrice> stockPrices(String ticker, LocalDate fromDx, int period);
    Collection<StockPrice> stockPrices(String ticker, int period);
}
