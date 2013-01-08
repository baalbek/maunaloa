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
}
