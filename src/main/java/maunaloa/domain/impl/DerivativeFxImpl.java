package maunaloa.domain.impl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import oahu.exceptions.BinarySearchException;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Derivative;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;
import oahux.domain.DerivativeFx;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 8/1/13
 * Time: 9:21 AM
 */
public class DerivativeFxImpl implements DerivativeFx {

    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    private final OptionCalculator calculator;
    private final Derivative derivative;

    public DerivativeFxImpl(Derivative derivative,
                            OptionCalculator calculator) {
        this.derivative = derivative;
        this.calculator = calculator;
    }

    //--------------------------------------------------
    //------------- Checked
    //--------------------------------------------------
    private BooleanProperty isChecked = new SimpleBooleanProperty(false);

    @Override
    public BooleanProperty isCheckedProperty() {
        return isChecked;
    }

    //--------------------------------------------------
    //------------- Risk
    //--------------------------------------------------
    private void logSetRiskExeption(Exception ex, double value) {
        log.warn(String.format("(%s) spot: %.2f, sell: %.2f, value: %.2f, exeption: %s",
                getTicker(),
                getParent().getValue(),
                getSell(),
                value,
                ex.getMessage()));

    }
    private DoubleProperty risk = new SimpleDoubleProperty(0.0);
    @Override
    public DoubleProperty riskProperty() {
        return risk;
    }
    @Override
    public void setRisk(double value) {
        risk.set(value);

        try {
            stockPriceRiskProperty().set(calculator.stockPriceFor(getSell() - value,this));
        }
        catch (BinarySearchException bex) {
            logSetRiskExeption(bex, value);
            stockPriceRiskProperty().set(-1.0);
        }
        catch (Exception ex) {
            logSetRiskExeption(ex,value);
            stockPriceRiskProperty().set(-1.0);
        }
    }
    private DoubleProperty stockPriceRisk = new SimpleDoubleProperty(0.0);
    @Override
    public DoubleProperty stockPriceRiskProperty() {
        return stockPriceRisk;
    }

    private Double _delta = null;
    @Override
    public double getDelta() {
        if (_delta == null) {
            _delta = calculator.delta(this);
        }
        return _delta;
    }

    private Double _spread = null;
    @Override
    public double getSpread() {
        if (_spread == null) {
            _spread = calculator.spread(this);
        }
        return _spread;
    }

    private Double _breakEven = null;
    @Override
    public double getBreakeven() {
        if (_breakEven == null) {
            _breakEven = calculator.breakEven(this);
        }
        return _breakEven;
    }



    //--------------------------------------------------
    //------------- Parent
    //--------------------------------------------------
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

    @Override
    public double getWatermark() {
        throw new NotImplementedException();
    }

    @Override
    public void setWatermark(double v) {
        throw new NotImplementedException();
    }

    @Override
    public void updateWatermark(double v) {
        throw new NotImplementedException();
    }

    @Override
    public double getDiffFromBought() {
        throw new NotImplementedException();
    }

    @Override
    public void setDiffFromBought(double v) {
        throw new NotImplementedException();
    }
}
