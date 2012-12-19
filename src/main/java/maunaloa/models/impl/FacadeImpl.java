package maunaloa.models.impl;

import maunaloa.models.NetfondsModel;
import maunaloa.models.mybatis.StockMapper;
import maunaloa.utils.MyBatisUtils;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
import oahu.financial.Etrade;
import oahu.models.MaunaloaFacade;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 11/22/12
 * Time: 6:46 PM
 */
public class FacadeImpl implements MaunaloaFacade {
    private final NetfondsModel netfondsModel;
    private final Etrade etrade;
    public FacadeImpl(NetfondsModel netfondsModel,
                      Etrade etrade) {
        this.netfondsModel = netfondsModel;
        this.etrade = etrade;
    }
    @Override
    public List<StockBean> stockPrices(String ticker, Date fromDx, int period) {
        SqlSession session = MyBatisUtils.getSession();
        List<StockBean> result = null;
        try {
            StockMapper mapper = session.getMapper(StockMapper.class);
            result = mapper.selectTicker(ticker, fromDx);
        }
        finally {
            session.close();
        }
        return result;
    }

    @Override
    public StockBean spot(String ticker) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DerivativeBean> calls(String ticker) {
        return etrade.getCalls(ticker);
    }

    @Override
    public List<DerivativeBean> puts(String ticker) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
