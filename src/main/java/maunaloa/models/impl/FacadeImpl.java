package maunaloa.models.impl;

import javafx.scene.Node;
import maunaloa.utils.DateUtils;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Derivative;
import oahu.financial.Etrade;
import oahu.financial.Stock;
import oahux.models.MaunaloaFacade;

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

    public FacadeImpl()  {
        setDefaultStartDate(DateUtils.createDate(2010, 1, 1));
    }

    //region Interface Methods
    @Override
    public Collection<Stock> stockPrices(String ticker, Date fromDx, int period) {
        /*
        SqlSession session = MyBatisUtils.getSession();
        List<StockBean> result = null;
        try {
            StockMapper mapper = session.getMapper(StockMapper.class);

            result = mapper.selectStocks(stockTicker.findId(ticker), fromDx);

        }
        finally {
            session.close();
        }
        return result;
        //*/
        return null;
    }

    @Override
    public Collection<Stock> stockPrices(String ticker, int period) {
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
        return null;
    }

    @Override
    public Stock spot(String ticker) {
        return null;  //return getEtrade().getSpot(ticker);
    }

    @Override
    public Collection<Derivative> calls(String ticker) {
        return null; //getEtrade().getCalls(ticker);
    }

    @Override
    public Collection<Derivative> puts(String ticker) {
        return null; //getEtrade().getPuts(ticker);
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
