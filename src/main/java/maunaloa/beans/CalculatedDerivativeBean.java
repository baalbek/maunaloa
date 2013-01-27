package maunaloa.beans;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import maunaloa.utils.DateUtils;
import oahu.financial.OptionCalculator;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 07.01.13
 * Time: 13:00
 */
public class CalculatedDerivativeBean extends DerivativeBean {
    private final OptionCalculator calculator;
    public CalculatedDerivativeBean(String ticker,
                          int opType,
                          double x,
                          double buy,
                          double sell,
                          Date expiry,
                          StockBean parent,
                          OptionCalculator calculator) {
        super(ticker,opType,x,buy,sell,expiry,parent);
        this.calculator = calculator;
    }

    //--------------------------------------------------
    //------------- days
    //--------------------------------------------------
    private SimpleDoubleProperty days;
    public DoubleProperty daysProperty() {
        if (days == null) {
            days = new SimpleDoubleProperty(DateUtils.diffDays(getExpiry()));
        }
        return days;
    }
    //--------------------------------------------------
    //------------- ivBuy
    //--------------------------------------------------
    private SimpleDoubleProperty ivBuy;
    public DoubleProperty ivBuyProperty() {
        if (ivBuy == null) {
            ivBuy = new SimpleDoubleProperty(calculator.iv(this,DerivativeBean.BUY));
        }
        return ivBuy;
    }
    public double getIvBuy() {
        return ivBuyProperty().get();
    }
    //--------------------------------------------------
    //------------- ivSell
    //--------------------------------------------------
    private SimpleDoubleProperty ivSell;
    public DoubleProperty ivSellProperty() {
        if (ivSell == null) {
            ivSell = new SimpleDoubleProperty(calculator.iv(this,DerivativeBean.SELL));
        }
        return ivSell;
    }
    public double getIvSell() {
        return ivSellProperty().get();
    }
    //--------------------------------------------------
    //------------- Delta
    //--------------------------------------------------
    private SimpleDoubleProperty delta;
    public DoubleProperty deltaProperty() {
        if (delta == null) {
            delta = new SimpleDoubleProperty(calculator.delta(this));
        }
        return delta;
    }
    public double getDelta() {
        return deltaProperty().get();
    }

    //--------------------------------------------------
    //------------- Break-even
    //--------------------------------------------------
    private SimpleDoubleProperty breakeven;
    public DoubleProperty breakevenProperty() {
        if (breakeven == null) {
            breakeven = new SimpleDoubleProperty(calculator.breakEven(this));
        }
        return breakeven;
    }
    public double getBreakeven() {
        return breakevenProperty().get();
    }

    //--------------------------------------------------
    //------------- Spread
    //--------------------------------------------------
    private SimpleDoubleProperty spread;
    public DoubleProperty spreadProperty() {
        if (spread == null) {
            spread = new SimpleDoubleProperty(calculator.spread(this));
        }
        return spread;
    }
    public double getSpread() {
        return spreadProperty().get();
    }


    //--------------------------------------------------
    //------------- Risk
    //--------------------------------------------------
    private DoubleProperty risk; // = new SimpleDoubleProperty(0.0);
    public double getRisk() {
        return riskProperty().get();
    }

    public void setRisk(double value) {
        riskProperty().set(value);
        stockPriceRiskProperty().set(calculator.stockPriceFor(getSell() - value,this,0));
    }
    public DoubleProperty riskProperty() {
        if (risk == null) {
            risk = new SimpleDoubleProperty(0.0);
        }
        return risk;
    }
    //--------------------------------------------------
    //------------- Stock Price Risk
    //--------------------------------------------------
    private DoubleProperty stockPriceRisk;
    public double getStockPriceRisk() {
        return stockPriceRiskProperty().get();
    }

    public void setStockPriceRisk(double value) {
        stockPriceRiskProperty().set(value);
    }
    public DoubleProperty stockPriceRiskProperty() {
        if (stockPriceRisk == null) {
            stockPriceRisk = new SimpleDoubleProperty(0.0);
        }
        return stockPriceRisk;
    }
    //--------------------------------------------------
    //------------- Is Calculable
    //--------------------------------------------------
    public boolean isCalculable() {
        if (getParent()== null) return false;
        if (daysProperty().get() < 10) return false;
        return true;
    }
}
