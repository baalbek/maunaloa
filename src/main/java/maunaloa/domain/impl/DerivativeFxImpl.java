package maunaloa.domain.impl;

import oahu.financial.Derivative;
import oahu.financial.StockPrice;
import oahux.domain.DerivativeFx;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 8/1/13
 * Time: 9:21 AM
 */
public class DerivativeFxImpl implements DerivativeFx {
    private final Derivative derivative;

    public DerivativeFxImpl(Derivative derivative) {
        this.derivative = derivative;

    }
    @Override
    public boolean getIsChecked() {
        return false;
    }

    @Override
    public void setIsChecked(boolean b) {
    }

    @Override
    public StockPrice getParent() {
        return derivative.getParent();
    }

    @Override
    public int getOpType() {
        return derivative.getOpType();
    }

    @Override
    public String getOpTypeStr() {
        return derivative.getOpTypeStr();
    }

    @Override
    public double getX() {
        return derivative.getX();
    }

    @Override
    public double getDays() {
        return derivative.getDays();
    }

    @Override
    public double getIvBuy() {
        return derivative.getIvBuy();
    }

    @Override
    public double getIvSell() {
        return derivative.getIvSell();
    }

    @Override
    public double getBuy() {
        return derivative.getBuy();
    }

    @Override
    public double getSell() {
        return derivative.getSell();
    }

    @Override
    public String getTicker() {
        return derivative.getTicker();
    }

    @Override
    public int getOid() {
        return derivative.getOid();
    }

    @Override
    public Date getExpiry() {
        return derivative.getExpiry();
    }

    @Override
    public String getSeries() {
        return derivative.getSeries();
    }
}
