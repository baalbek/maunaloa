package maunaloa.repository.impl;

/**
 * Created by rcs on 4/14/14.
 */
import maunaloa.repository.StockRepository;
import maunaloa.service.MyBatisUtils;
import oahu.financial.Stock;
import oahu.financial.StockLocator;
import oahu.financial.StockPrice;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import ranoraraku.models.mybatis.StockMapper;

import java.util.Collection;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

public class DefaultStockRepository implements StockRepository {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private StockLocator locator;
    private LocalDate defaultStartDate;

    //region interface StockRepository
    @Override
    public List<Stock> getStocks() {
        return getLocator().getTickers();
    }
    @Override
    public Collection<StockPrice> stockPrices(String ticker, LocalDate fromDx, int period) {
        int tickId = getLocator().findId(ticker);

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
    //endregion interface StockRepository

    //region Properties
    public StockLocator getLocator() {
        return locator;
    }

    public void setLocator(StockLocator locator) {
        this.locator = locator;
    }
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
