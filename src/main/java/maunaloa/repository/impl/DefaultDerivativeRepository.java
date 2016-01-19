package maunaloa.repository.impl;

import maunaloa.derivatives.DerivativeFxImpl;
import maunaloa.repository.DerivativeRepository;
import oahu.dto.Tuple3;
import oahu.financial.DerivativePrice;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;
import oahu.financial.repository.EtradeDerivatives;
import oahux.financial.DerivativeFx;

import java.util.*;
import java.util.function.Function;

/**
 * Created by rcs on 09.01.16.
 *
 */
public class DefaultDerivativeRepository implements DerivativeRepository {

    //region interface DerivativeRepository
    @SuppressWarnings("unchecked")
    @Override
    public Collection<DerivativeFx> getCalls(String ticker) {
        return (Collection<DerivativeFx>)getItems(ticker, Tuple3::second);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<DerivativeFx> getPuts(String ticker) {
        return (Collection<DerivativeFx>)getItems(ticker, Tuple3::third);
    }

    @Override
    public Optional<StockPrice> getSpot(String ticker) {
        return Optional.empty();
    }
    //endregion Public Methods

    //region Private Methods
    private Collection<DerivativeFx> toDerivativeFx(Collection<DerivativePrice> derivs) {
        Collection<DerivativeFx> result = new ArrayList<>();

        for (DerivativePrice d : derivs) {
            result.add(new DerivativeFxImpl(d,calculator));
        }
        return result;
    }

    private Map<String,Tuple3<StockPrice,Collection<DerivativeFx>,Collection<DerivativeFx>>> items;

    @SuppressWarnings("unchecked")
    private Object getItems(String ticker,
                            Function<Tuple3<StockPrice,Collection<DerivativeFx>,Collection<DerivativeFx>>,
                                                                Object> fn) {
        if (items == null) {
            items = new HashMap<>();
        }
        Tuple3<StockPrice,Collection<DerivativeFx>,Collection<DerivativeFx>>  result = items.get(ticker);
        if (result == null) {
            Tuple3<StockPrice,Collection<DerivativePrice>,Collection<DerivativePrice>> tmp =
                    etrade.getSpotCallsPuts(ticker);
            result = new Tuple3(tmp.first(),toDerivativeFx(tmp.second()),toDerivativeFx(tmp.third()));
            items.put(ticker,result);
        }
        return fn.apply(result);
    }
    //endregion Private Methods

    //region Properties
    private EtradeDerivatives etrade;
    private OptionCalculator calculator;

    public void setEtrade(EtradeDerivatives etrade) {
        this.etrade = etrade;
    }

    public void setCalculator(OptionCalculator calculator) {
        this.calculator = calculator;
    }
    //endregion Properties
}
