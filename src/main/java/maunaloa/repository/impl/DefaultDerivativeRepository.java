package maunaloa.repository.impl;

import maunaloa.financial.DerivativeFxImpl;
import maunaloa.repository.DerivativeRepository;
import maunaloa.service.MyBatisUtils;
import oahu.financial.Derivative;
import oahu.financial.Etrade;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;
import org.apache.ibatis.session.SqlSession;
import ranoraraku.beans.DerivativeBean;
import ranoraraku.beans.OptionPurchaseBean;
import ranoraraku.beans.OptionPurchaseWithDerivativeBean;
import ranoraraku.models.mybatis.CritterMapper;
import ranoraraku.models.mybatis.DerivativeMapper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by rcs on 4/15/14.
 */
public class DefaultDerivativeRepository implements DerivativeRepository {
    private Collection<DerivativeFx> toDerivativeFx(Collection<Derivative> derivs) {
        Collection<DerivativeFx> result = new ArrayList<>();

        for (Derivative d : derivs) {
            result.add(new DerivativeFxImpl(d,calculator));
        }
        return result;

    }
    @Override
    public Collection<DerivativeFx> calls(String ticker) {
        return toDerivativeFx(getEtrade().getCalls(ticker));
    }

    @Override
    public Collection<DerivativeFx> puts(String ticker) {
        return toDerivativeFx(getEtrade().getPuts(ticker));
    }

    @Override
    public StockPrice spot(String ticker) {
        return getEtrade().getSpot(ticker);
    }

    @Override
    public void invalidate() {
        getEtrade().invalidate();
    }

    @Override
    public void registerOptionPurchase(OptionPurchaseBean purchase) {

        SqlSession session = MyBatisUtils.getSession();

        try {
            /*
            CritterMapper mapper = session.getMapper(CritterMapper.class);

            mapper.insertPurchase(purchase);

            session.commit();
            */
            DerivativeMapper dmapper = session.getMapper(DerivativeMapper.class);

            //int opid = dmapper.derivativeIdFor(purchase.getTicker());

            DerivativeBean dbBean = dmapper.findDerivative(purchase.getOptionName());
            if (dbBean == null) {
                OptionPurchaseWithDerivativeBean purchaseWithDeriv = (OptionPurchaseWithDerivativeBean)purchase;
                //DerivativeBean curDerivative = purchaseWithDeriv.getDerivative();
            }

            System.out.println(dbBean.getOid());
            //System.out.println("Opid: " + dbBean.getOid());
        }
        finally {
            session.close();
        }
    }

    @Override
    public void registerOptionPurchase(Derivative d, int purchaseType, int volume) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            DerivativeMapper dmapper = session.getMapper(DerivativeMapper.class);
            DerivativeBean dbBean = dmapper.findDerivative(d.getTicker());
            if (dbBean == null) {
                //insert into stockmarket.optionx (opname, strike, exp_date, optype, stock_id, series)
                //values (#{ticker}, #{x}, #{expiry}, #{opTypeStr}, #{stockId}, #{series})
                /*
                System.out.println("Ticker: " + d.getTicker() +
                                   ", x: " + d.getExpiry() +
                                    ", opTypeStr: " + d.getOpTypeStr() +
                                    ", stockId: " + d.getParent().getStock().getOid() +
                                    ", series: " + d.getSeries());
                */
                dbBean = new DerivativeBean();
                dbBean.setTicker(d.getTicker());
                dbBean.setExpiry(d.getExpiry());
                dbBean.setOpTypeStr(d.getOpTypeStr());
                dbBean.setParent(d.getParent());
                dmapper.insertDerivative(dbBean);
            }
            CritterMapper cmapper = session.getMapper(CritterMapper.class);
            OptionPurchaseWithDerivativeBean newPurchase = new OptionPurchaseWithDerivativeBean();
            d.setOid(dbBean.getOid());
            newPurchase.setDerivative(d);
            newPurchase.setDx(new java.util.Date());
            newPurchase.setVolume(volume);
            newPurchase.setStatus(1);
            newPurchase.setPurchaseType(purchaseType);
            newPurchase.setSpotAtPurchase(d.getParent().getCls());
            cmapper.insertPurchase(newPurchase);
            session.commit();
        }
        finally {
            session.close();
        }
    }

    //region Properties
    private Etrade etrade;
    private OptionCalculator calculator;

    public Etrade getEtrade() {
        return etrade;
    }

    public void setEtrade(Etrade etrade) {
        this.etrade = etrade;
    }

    public OptionCalculator getCalculator() {
        return calculator;
    }

    public void setCalculator(OptionCalculator calculator) {
        this.calculator = calculator;
    }


    //endregion Properties
}
