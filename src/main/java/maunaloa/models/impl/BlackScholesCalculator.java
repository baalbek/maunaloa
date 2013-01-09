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
        CalculatedDerivativeBean cd = (CalculatedDerivativeBean)d;
        double newSpot = cd.getParent().getValue() + 1.0;
        double newPrice = cd.getOpType() == DerivativeBean.CALL ?
                _helper.callPrice(newSpot, cd.getX(), 0.05, yearsToExpiry(cd), cd.getIvSell()) :
                _helper.putPrice(newSpot, cd.getX(), 0.05, yearsToExpiry(cd), cd.getIvSell());
        return newPrice - cd.getSell();

    }

    @Override
    public double spread(DerivativeBean d) {
        return d.getSell() - d.getBuy();
    }

    @Override
    public double breakEven(DerivativeBean d) {
        CalculatedDerivativeBean cd = (CalculatedDerivativeBean)d;
        return _helper.stockPrice(d.getSell(), cd.getOpType(), cd.getX(), 0.05, yearsToExpiry(cd), cd.getIvBuy(), -1.0);
    }

    @Override
    public double stockPriceFor(double optionPrice, DerivativeBean d, int priceType) {
        CalculatedDerivativeBean cd = (CalculatedDerivativeBean)d;
        return _helper.stockPrice(optionPrice, cd.getOpType(), cd.getX(), 0.05, yearsToExpiry(cd), cd.getIvBuy(), -1.0);
    }

    @Override
    public double iv(DerivativeBean d, int priceType) {
        double price = priceType == DerivativeBean.BUY ? d.getBuy() : d.getSell();
        return d.getOpType() == DerivativeBean.CALL ?
                _helper.ivCall(price, d.getParent().getValue(), d.getX(), 0.05, yearsToExpiry((CalculatedDerivativeBean)d)) :
                _helper.ivPut(price, d.getParent().getValue(), d.getX(), 0.05, yearsToExpiry((CalculatedDerivativeBean)d));
    }
}
