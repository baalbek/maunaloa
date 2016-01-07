package maunaloa.stocks;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import oahu.financial.StockPrice;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 9/16/13
 * Time: 10:02 PM
 */
public class StockPriceFx {
    private StockPrice price;

    public StockPriceFx() {
    }

    private DoubleProperty _opnProperty = new SimpleDoubleProperty(0.0);
    public DoubleProperty opnProperty() {
        return _opnProperty;
    }

    private DoubleProperty _hiProperty = new SimpleDoubleProperty(1.0);
    public DoubleProperty hiProperty() {
        return _hiProperty;
    }

    private DoubleProperty _loProperty = new SimpleDoubleProperty(2.0);
    public DoubleProperty loProperty() {
        return _loProperty;
    }

    private DoubleProperty _clsProperty = new SimpleDoubleProperty(3.0);
    public DoubleProperty clsProperty() {
        return _clsProperty;
    }

    public StockPrice getPrice() {
        return price;
    }

    public void setPrice(StockPrice price) {
        this.price = price;
        opnProperty().set(this.price.getOpn());
        hiProperty().set(this.price.getHi());
        loProperty().set(this.price.getLo());
        clsProperty().set(this.price.getCls());
    }
}
