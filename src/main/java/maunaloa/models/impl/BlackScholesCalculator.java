package maunaloa.models.impl;

import maunaloa.beans.CalculatedDerivativeBean;
import oahu.financial.OptionCalculator;
import oahu.financial.beans.DerivativeBean;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/6/13
 * Time: 3:32 PM
 */
public class BlackScholesCalculator implements OptionCalculator {

    private final kalihiwai.financial.OptionCalculator _helper;

    public BlackScholesCalculator(kalihiwai.financial.OptionCalculator helper) {
        _helper = helper;
    }

    private double yearsToExpiry(CalculatedDerivativeBean d) {
        return d.daysProperty().get()/365.0;
    }

    @Override
    public double delta(DerivativeBean d) {
        /*
        double newSpot = d.getParent().getValue() + 1.0;
        double newPrice = d.getOpType() == Derivative.CALL ?
                _helper.callPrice(newSpot, d.getX(), 0.05, yearsToExpiry(d), d.ivSell()) :
                _helper.putPrice(newSpot, d.getX(), 0.05, yearsToExpiry(d), d.ivSell());
        return newPrice - d.getSell();
        */
        return 0.0;
    }

    @Override
    public double spread(DerivativeBean d) {
        //return d.getSell() - d.getBuy();
        return 0.0;
    }

    @Override
    public double breakEven(DerivativeBean d) {
        //return _helper.stockPrice(d.getSell(), d.getOpType(), d.getX(), 0.05, yearsToExpiry(d), d.ivBuy(), -1.0);
        return 0.0;
    }

    @Override
    public double stockPriceFor(double optionPrice, DerivativeBean o, int priceType) {
        //return _helper.stockPrice(optionPrice, d.getOpType(), d.getX(), 0.05, yearsToExpiry(d), d.ivBuy(), -1.0);
        return 100.0;
    }

    @Override
    public double iv(DerivativeBean d, int priceType) {
        /*
        double price = priceType == DerivativeBean.BUY ? d.getBuy() : d.getSell();
        return d.getOpType() == DerivativeBean.CALL ?
                _helper.ivCall(price, d.getParent().getValue(), d.getX(), 0.05, yearsToExpiry((CalculatedDerivativeBean)d)) :
                _helper.ivPut(price, d.getParent().getValue(), d.getX(), 0.05, yearsToExpiry((CalculatedDerivativeBean)d));
        */
        return 0.45;
    }
}
