package maunaloa.models.impl;

import maunakea.financial.beans.CalculatedDerivativeBean;
import oahu.financial.OptionCalculator;
import oahu.financial.beans.DerivativeBean;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/6/13
 * Time: 3:32 PM
 */
public class KalihiwaiBlackScholesCalculator implements OptionCalculator {

    private kalihiwai.financial.OptionCalculator _helper;

    public KalihiwaiBlackScholesCalculator() {}

    public KalihiwaiBlackScholesCalculator(kalihiwai.financial.OptionCalculator helper) {
        _helper = helper;
    }

    private double yearsToExpiry(CalculatedDerivativeBean d) {
        return d.daysProperty().get()/365.0;
    }

    @Override
    public double delta(DerivativeBean d) {
        CalculatedDerivativeBean cd = (CalculatedDerivativeBean)d;
        double iv = cd.getIvSell();

        if (iv < 0.0) return cd.getOpType() == DerivativeBean.CALL ? -1.0 : 1.0;

        double newSpot = cd.getParent().getValue() + 1.0;
        double newPrice = cd.getOpType() == DerivativeBean.CALL ?
                _helper.callPrice(newSpot, cd.getX(), 0.05, yearsToExpiry(cd), iv) :
                _helper.putPrice(newSpot, cd.getX(), 0.05, yearsToExpiry(cd), iv);
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
        CalculatedDerivativeBean cd = (CalculatedDerivativeBean)d;

        double price = priceType == DerivativeBean.BUY ? cd.getBuy() : cd.getSell();
        return cd.getOpType() == DerivativeBean.CALL ?
                _helper.ivCall(price, cd.getParent().getValue(), cd.getX(), 0.05, yearsToExpiry(cd)) :
                _helper.ivPut(price, cd.getParent().getValue(), cd.getX(), 0.05, yearsToExpiry(cd));
    }
}
