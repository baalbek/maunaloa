package maunaloa.beans;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    //------------- ivBuy
    //--------------------------------------------------
    private SimpleDoubleProperty ivBuy;
    public double getIvBuy() {
        if (ivBuy == null) {
            ivBuy = new SimpleDoubleProperty(0.35);
        }
        return ivBuy.get();
    }
    public void setIvBuy(double value) {
        if (ivBuy == null) {
            ivBuy = new SimpleDoubleProperty(value);
        }
        else {
            ivBuy.set(value);
        }
    }
    public DoubleProperty ivBuyProperty() {
        return ivBuy;
    }
}
