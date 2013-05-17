package maunaloa.models.impl;

import oahu.exceptions.NotImplementedException;
import oahu.financial.OptionCalculator;
import oahu.financial.beans.DerivativeBean;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 5/14/13
 * Time: 11:42 AM
 */
public class BlackScholesCalculator implements OptionCalculator {
    @Override
    public double delta(DerivativeBean d) {
        return 0.0;
    }

    @Override
    public double spread(DerivativeBean d) {
        return 0.0;
    }

    @Override
    public double breakEven(DerivativeBean d) {
        return 0.0;
    }

    @Override
    public double stockPriceFor(double optionPrice, DerivativeBean o, int priceType) {
        return 0.0;
    }

    @Override
    public double iv(DerivativeBean d, int priceType) {
        return 0.0;
    }
}
