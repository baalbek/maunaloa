package maunaloa.financial;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import oahu.exceptions.BinarySearchException;
import oahu.financial.Derivative;
import oahu.financial.DerivativePrice;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;
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
    private final DerivativePrice derivativePrice;

    public DerivativeFxImpl(DerivativePrice derivativePrice,
                            OptionCalculator calculator) {
        this.derivativePrice = derivativePrice;
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
                "ticker", //TODO-rcs getTicker(),
                0.0, //TODO-rcs getParent().getValue(),
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
            _breakEven = calculator.stockPriceFor(getSell(),this);
        }
        return _breakEven;
    }



    //--------------------------------------------------
    //------------- Parent
    //--------------------------------------------------
    @Override
    public Derivative getDerivative() {
        return null;
    }

    @Override
    public StockPrice getStockPrice() {
        return null;
    }

    @Override
    public double getDays() {
        return derivativePrice.getDays();
    }

    @Override
    public double getIvBuy() {
        return derivativePrice.getIvBuy();
    }

    @Override
    public double getIvSell() {
        return derivativePrice.getIvSell();
    }

    @Override
    public double getBuy() {
        return derivativePrice.getBuy();
    }

    @Override
    public double getSell() {
        return derivativePrice.getSell();
    }

    @Override
    public int getOid() {
        return derivativePrice.getOid();
    }

    @Override
    public void setOid(int oid) {
        derivativePrice.setOid(oid);
    }
}
