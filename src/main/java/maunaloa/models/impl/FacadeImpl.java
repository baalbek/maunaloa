package maunaloa.models.impl;

import javafx.scene.Node;
import maunaloa.utils.DateUtils;
import oahu.exceptions.NotImplementedException;
import oahu.financial.*;
import oahux.models.MaunaloaFacade;
import org.apache.ibatis.session.SqlSession;
import ranoraraku.beans.StockPriceBean;
import ranoraraku.models.mybatis.StockMapper;
import ranoraraku.utils.MyBatisUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 11/22/12
 * Time: 6:46 PM
 */
public class FacadeImpl implements MaunaloaFacade {
    private Etrade etrade;
    private Date defaultStartDate;

    Map<String, Collection<Stock>> stockBeansMap = new HashMap<>();
    private StockLocator locator;

    public FacadeImpl()  {
       setDefaultStartDate(DateUtils.createDate(2010, 1, 1));
    }

    //region Interface Methods
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
        /*
        if (stockBeansMap.containsKey(ticker)) {
            return stockBeansMap.get(ticker);
        }
        else {
            Collection<StockBean> beans = stockPrices(ticker, getDefaultStartDate(), period);
            stockBeansMap.put(ticker,beans);
            return beans;
        }
        //*/
        return stockPrices(ticker, getDefaultStartDate(), period);
    }

    @Override
    public StockPrice spot(String ticker) {
        return getEtrade().getSpot(ticker);
    }

    @Override
    public Collection<Derivative> calls(String ticker) {
        return getEtrade().getCalls(ticker);
    }

    @Override
    public Collection<Derivative> puts(String ticker) {
        return getEtrade().getPuts(ticker);
    }

    @Override
    public Collection<Derivative> callsAndPuts(String ticker) {
        return null; //calls(ticker);
    }

    public Collection<Node> fibLines(String ticker) {
        throw new NotImplementedException();
    }

    public void addFibLine(String ticker, Node line) {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getTickers() {
        return getLocator().getTickers();
    }
    //endregion Interface Methods


    //region Properties

    public Etrade getEtrade() {
        return etrade;
    }

    public void setEtrade(Etrade etrade) {
        this.etrade = etrade;
    }

    public Date getDefaultStartDate() {
        return defaultStartDate;
    }

    public void setDefaultStartDate(Date defaultStartDate) {
        this.defaultStartDate = defaultStartDate;
    }

    public StockLocator getLocator() {
        return locator;
    }

    public void setLocator(StockLocator locator) {
        this.locator = locator;
    }
    /*
    public StockTicker getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(StockTicker stockTicker) {
        this.stockTicker = stockTicker;
    }
    //*/

    //endregion Properties
}
