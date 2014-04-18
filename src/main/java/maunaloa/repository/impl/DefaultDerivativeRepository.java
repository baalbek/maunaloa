package maunaloa.repository.impl;

import maunaloa.financial.DerivativeFxImpl;
import maunaloa.repository.DerivativeRepository;
import oahu.financial.Derivative;
import oahu.financial.Etrade;
import oahu.financial.OptionCalculator;
import oahux.financial.DerivativeFx;

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
