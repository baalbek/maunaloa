package maunaloa.repository.impl;

/**
 * Created by rcs on 4/14/14.
 */
import maunaloa.repository.StockRepository;
import maunaloa.services.MyBatisUtils;
import oahu.financial.Stock;
import oahu.financial.StockLocator;
import oahu.financial.StockPrice;
import org.apache.ibatis.session.SqlSession;
import ranoraraku.models.mybatis.StockMapper;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DefaultStockRepository implements StockRepository {
    private StockLocator locator;
    private Date defaultStartDate;

    //region interface StockRepository
    @Override
    public List<Stock> getStocks() {
        return getLocator().getTickers();
    }
    @Override
    public Collection<StockPrice> stockPrices(String ticker, Date fromDx, int period) {
        SqlSession session = MyBatisUtils.getSession();
        List<StockPrice> result = null;
        try {
            StockMapper mapper = session.getMapper(StockMapper.class);

            Stock stock = mapper.selectStockWithPrices(locator.findId(ticker), fromDx);

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
    public Date getDefaultStartDate() {
        return defaultStartDate;
    }
    public void setDefaultStartDate(Date defaultStartDate) {
        this.defaultStartDate = defaultStartDate;
    }
    //endregion Properties
}
