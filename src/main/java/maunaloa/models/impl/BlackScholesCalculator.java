package maunaloa.models.impl;

import oahu.financial.OptionCalculator;
import oahu.financial.beans.DerivativeBean;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/6/13
 * Time: 3:32 PM
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
        double price = priceType == DerivativeBean.BUY ? o.getBuy() : o.getSell();


        return 100.0;
    }

    @Override
    public double iv(DerivativeBean d, int priceType) {
        return 0.24;
    }
}
