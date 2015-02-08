package maunaloa.repository.impl;

import maunaloa.repository.StockIndexRepository;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahu.financial.repository.EtradeStockIndex;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultStockIndexRepository implements StockIndexRepository {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private Map<String,StockPrice> items;

    //region Interface StockIndexRepository 
    @Override
    public StockPrice getSpot(String ticker) {
        if (items == null) {
            items = new HashMap<>();
        }

        StockPrice result = items.get(ticker);
        if (result == null) {
            log.info(String.format("Result was null for ticker %s. Relolading the cache...",ticker));
            Collection<StockPrice> prices = etrade.getSpots();
            for (StockPrice s : prices) {
                Stock stock = s.getStock();
                String curTicker = stock.getTicker();
                items.put(stock.getTicker(), s);
                log.info(String.format("Got StockPrice for %s",curTicker));
                if ((s != null) && (curTicker.equals(ticker))) {
                    result = s;
                }
            }
        }
        return result;
    }
    @Override
    public void invalidate() {
        items = null;
    }
    //endregion Interface StockIndexRepository 

    //region Properties
    private EtradeStockIndex etrade;

    public EtradeStockIndex getEtrade() {
        return etrade;
    }

    public void setEtrade(EtradeStockIndex etrade) {
        this.etrade = etrade;
    }
    //endregion Properties
}
