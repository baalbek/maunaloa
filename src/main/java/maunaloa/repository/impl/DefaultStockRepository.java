package maunaloa.repository.impl;

/**
 * Created by rcs on 4/14/14.
 */
import org.apache.log4j.Logger;
import ranoraraku.models.impl.StockMarketReposImpl;

import java.time.LocalDate;

public class DefaultStockRepository extends StockMarketReposImpl { // implements StockMarketRepository {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private LocalDate defaultStartDate;
    
    //region interface StockMarketRepository 

    //endregion interface StockMarketRepository 
    
    //region interface StockRepository
    /*
    @Override
    public Collection<Stock> getStocks() {
        return null;
    }
    @Override
    public Collection<StockPrice> stockPrices(String ticker, LocalDate fromDx, int period) {

        return null;
        int tickId = repos.findId(ticker);

        log.debug(String.format("Ticker %s, ticker id %d, date %s", ticker,tickId, fromDx));

        SqlSession session = MyBatisUtils.getSession();
        List<StockPrice> result = null;
        try {
            StockMapper mapper = session.getMapper(StockMapper.class);

            Stock stock = mapper.selectStockWithPrices(tickId, Date.valueOf(fromDx));

            result = stock.getPrices();

        }
        finally {
            session.close();
        }
        return result;
    }
    @Override
    public Collection<StockPrice> stockPrices(String ticker, int period) {
        return stockPrices(ticker,getDefaultStartDate(),period);
    }
    //*/
    //endregion interface StockRepository

    //region Properties
    public LocalDate getDefaultStartDate() {
        if (defaultStartDate == null) {
            defaultStartDate = LocalDate.of(2012,2,1);
        }
        return defaultStartDate;
    }
    public void setDefaultStartDate(LocalDate defaultStartDate) {
        this.defaultStartDate = defaultStartDate;
    }
    //endregion Properties
}
