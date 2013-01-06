package maunaloa.models.impl;

import oahu.financial.Derivative;
import oahu.financial.OptionCalculator;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/6/13
 * Time: 3:32 PM
 */
public class BlackScholesCalculator implements OptionCalculator {
    @Override
    public double delta(Derivative d) {
        return 0.0;
    }

    @Override
    public double spread(Derivative d) {
        return 0.0;
    }

    @Override
    public double breakEven(Derivative d) {
        return 0.0;
    }

    @Override
    public double stockPriceFor(double optionPrice, Derivative o, int priceType) {
        return 100.0;
    }

    @Override
    public double iv(Derivative d, int priceType) {
        return 0.24;
    }
}
