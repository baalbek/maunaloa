package maunaloa.models.impl;

import javafx.scene.Node;
import maunaloa.models.NetfondsModel;
import maunaloa.models.mybatis.StockMapper;
import maunaloa.utils.DateUtils;
import maunaloa.utils.MyBatisUtils;
import oahu.exceptions.NotImplementedException;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
import oahu.financial.Etrade;
import oahu.models.MaunaloaFacade;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 11/22/12
 * Time: 6:46 PM
 */
public class FacadeImpl implements MaunaloaFacade {
    private NetfondsModel netfondsModel;
    private Etrade etrade;
    private Date defaultStartDate;

    Map<String, Collection<StockBean>> stockBeansMap = new HashMap<>();

    public FacadeImpl()  {
        setDefaultStartDate(DateUtils.createDate(2010, 1, 1));
    }

    //region Interface Methods
    @Override
    public Collection<StockBean> stockPrices(String ticker, Date fromDx, int period) {
        SqlSession session = MyBatisUtils.getSession();
        List<StockBean> result = null;
        try {
            StockMapper mapper = session.getMapper(StockMapper.class);

            result = mapper.selectStocks(3, fromDx);

        }
        finally {
            session.close();
        }
        return result;
    }

    @Override
    public Collection<StockBean> stockPrices(String ticker, int period) {
        if (stockBeansMap.containsKey(ticker)) {
            return stockBeansMap.get(ticker);
        }
        else {
            Collection<StockBean> beans = stockPrices(ticker, getDefaultStartDate(), period);
            stockBeansMap.put(ticker,beans);
            return beans;
        }
    }

    @Override
    public StockBean spot(String ticker) {
        return getEtrade().getSpot(ticker);
    }

    @Override
    public Collection<DerivativeBean> calls(String ticker) {
        return getEtrade().getCalls(ticker);
    }

    @Override
    public Collection<DerivativeBean> puts(String ticker) {
        return getEtrade().getPuts(ticker);
    }

    @Override
    public Collection<DerivativeBean> callsAndPuts(String ticker) {
        return calls(ticker);
    }

    @Override
    public Collection<Node> fibLines(String ticker) {
        throw new NotImplementedException();
    }

    @Override
    public void addFibLine(String ticker, Node line) {
        throw new NotImplementedException();
    }
    //endregion Interface Methods


    //region Properties
    public NetfondsModel getNetfondsModel() {
        return netfondsModel;
    }

    public void setNetfondsModel(NetfondsModel netfondsModel) {
        this.netfondsModel = netfondsModel;
    }

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
    //endregion Properties
}
