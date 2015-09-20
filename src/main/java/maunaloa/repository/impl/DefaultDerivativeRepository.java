package maunaloa.repository.impl;

import maunaloa.financial.DerivativeFxImpl;
import maunaloa.repository.DerivativeRepository;
import oahu.dto.Tuple3;
import oahu.financial.DerivativePrice;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;
import oahu.financial.repository.EtradeDerivatives;
import oahux.financial.DerivativeFx;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by rcs on 4/15/14.
 *
 */
public class DefaultDerivativeRepository implements DerivativeRepository {
    private Map<String,Tuple3<StockPrice,Collection<DerivativeFx>,Collection<DerivativeFx>>> items;
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    private Collection<DerivativeFx> toDerivativeFx(Collection<DerivativePrice> derivs) {
        Collection<DerivativeFx> result = new ArrayList<>();

        for (DerivativePrice d : derivs) {
            result.add(new DerivativeFxImpl(d,calculator));
        }
        return result;
    }

    //private Collection<DerivativeFx> getItems(String ticker,
    private Object getItems(String ticker,
                            Function<Tuple3<StockPrice,Collection<DerivativeFx>,Collection<DerivativeFx>>,
                                     Object> fn) {
        if (items == null) {
            items = new HashMap<>();
        }
        Tuple3<StockPrice,Collection<DerivativeFx>,Collection<DerivativeFx>>  result = items.get(ticker);
        if (result == null) {
            Tuple3<StockPrice,Collection<DerivativePrice>,Collection<DerivativePrice>> tmp = getEtrade().getSpotCallsPuts(ticker);
            result = new Tuple3(tmp.first(),toDerivativeFx(tmp.second()),toDerivativeFx(tmp.third()));
            items.put(ticker,result);
        }
        return fn.apply(result);
    }


    //region interface DerivativeRepository
    @Override
    public Collection<DerivativeFx> getCalls(String ticker) {
        return (Collection<DerivativeFx>)getItems(ticker,(e) -> e.second());
    }

    @Override
    public Collection<DerivativeFx> getPuts(String ticker) {
        return (Collection<DerivativeFx>)getItems(ticker,(e) -> e.third());
    }

    @Override
    public StockPrice getSpot(String ticker) {
        return (StockPrice)getItems(ticker, (e) -> e.first());
    }

    @Override
    public void invalidate() {
        items = null;
    }
    //endregion interface DerivativeRepository

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
