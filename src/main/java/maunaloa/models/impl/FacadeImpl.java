package maunaloa.models.impl;

import javafx.scene.Node;
import maunaloa.domain.impl.DerivativeFxImpl;
import maunaloa.utils.DateUtils;
import oahu.annotations.Memoize;
import oahu.exceptions.NotImplementedException;
import oahu.financial.*;
import oahux.domain.DerivativeFx;
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
    private OptionCalculator calculator;

    Map<String, Collection<Stock>> stockBeansMap = new HashMap<>();
    private StockLocator locator;

    public FacadeImpl()  {
       setDefaultStartDate(DateUtils.createDate(2010, 1, 1));
    }

    //region Interface Methods
    @Override
    @Memoize
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
    @Memoize
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
    @Memoize
    public StockPrice spot(String ticker) {
        return getEtrade().getSpot(ticker);
    }

    private Collection<DerivativeFx> toDerivativeFx(Collection<Derivative> derivs) {
        Collection<DerivativeFx> result = new ArrayList<>();

        for (Derivative d : derivs) {
            result.add(new DerivativeFxImpl(d,calculator));
        }
        return result;

    }

    @Override
    @Memoize
    public Collection<DerivativeFx> calls(String ticker) {
        return toDerivativeFx(getEtrade().getCalls(ticker));
    }

    @Override
    @Memoize
    public Collection<DerivativeFx> puts(String ticker) {
        return toDerivativeFx(getEtrade().getPuts(ticker));
    }

    @Override
    public Collection<DerivativeFx> callsAndPuts(String ticker) {
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

    public OptionCalculator getCalculator() {
        return calculator;
    }

    public void setCalculator(OptionCalculator calculator) {
        this.calculator = calculator;
    }

    //endregion Properties
}
