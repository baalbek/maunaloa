package maunaloa.repository.impl;

import maunaloa.financial.DerivativeFxImpl;
import maunaloa.repository.DerivativeRepository;
import oahu.financial.DerivativePrice;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;
import oahu.financial.repository.EtradeDerivatives;
import oahux.financial.DerivativeFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by rcs on 4/15/14.
 *
 */
public class DefaultDerivativeRepository implements DerivativeRepository {
    private Map<String,Collection<DerivativeFx>> calls;
    private Map<String,Collection<DerivativeFx>> puts;
    private Map<String,StockPrice> spots;

    private Collection<DerivativeFx> toDerivativeFx(Collection<DerivativePrice> derivs) {
        Collection<DerivativeFx> result = new ArrayList<>();

        for (DerivativePrice d : derivs) {
            result.add(new DerivativeFxImpl(d,calculator));
        }
        return result;

    }
    //*
    private Collection<DerivativeFx> getItems(String ticker,
                                              Map<String,Collection<DerivativeFx>> currentRepos,
                                              Supplier<Collection<DerivativeFx>> factory) {
        if (currentRepos == null) {
            currentRepos = new HashMap<>();
        }
        Collection<DerivativeFx> result = currentRepos.get(ticker);
        if (result == null) {
            result  = factory.get();
            currentRepos.put(ticker,result);
        }
        return result;
    }
    //*/
    /*
    private static <T> Collection<T> getItems(String ticker,
                                   Map<String,Collection<T>> currentRepos,
                                   Supplier<Collection<T>> factory) {
        if (currentRepos == null) {
            currentRepos = new HashMap<>();
        }
        Collection<T> result = currentRepos.get(ticker);
        if (result == null) {
            result  = factory.get();
            currentRepos.put(ticker,result);
        }
        return result;
    }
    //*/

    @Override
    public Collection<DerivativeFx> getCalls(String ticker) {
        return getItems(ticker,calls, () -> toDerivativeFx(getEtrade().getCalls(ticker)));
    }

    @Override
    public Collection<DerivativeFx> getPuts(String ticker) {
        return getItems(ticker,puts, () -> toDerivativeFx(getEtrade().getPuts(ticker)));
    }

    @Override
    public StockPrice getSpot(String ticker) {
        if (spots == null) {
            spots = new HashMap<>();
        }
        StockPrice result = spots.get(ticker);
        if (result == null) {
            result = getEtrade().getSpot(ticker);
            spots.put(ticker,result);
        }
        //return getEtrade().getSpot(ticker);
        return result;
    }

    @Override
    public void invalidate() {
        if (spots != null) spots.clear();
        if (calls != null) calls.clear();
        if (puts != null) puts.clear();
    }
    /*
    @Override
    public void registerOptionPurchase(OptionPurchaseBean purchase) {

        SqlSession session = MyBatisUtils.getSession();

        try {
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
    public void registerOptionPurchase(DerivativePrice d, int purchaseType, int volume) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            DerivativeMapper dmapper = session.getMapper(DerivativeMapper.class);
            DerivativeBean dbBean = dmapper.findDerivative(d.getTicker());
            if (dbBean == null) {
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
    //*/

    //region Properties
    private EtradeDerivatives etrade;
    private OptionCalculator calculator;

    public EtradeDerivatives getEtrade() {
        return etrade;
    }

    public void setEtrade(EtradeDerivatives etrade) {
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
