package maunaloa.models.impl;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import oahu.beans.Tuple;
import oahu.financial.Derivative;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahu.financial.html.EtradeHtmlParser;
import org.apache.commons.lang.NotImplementedException;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/10/13
 * Time: 12:43 AM
 */
public class FilteredDerivativeParser implements EtradeHtmlParser {
    //private final OptionCalculator calculator;
    //private final HtmlRowBeanFactory factory;



    /*
    public FilteredDerivativeParser(OptionCalculator calculator,
                                   HtmlRowBeanFactory factory) {
        this.calculator = calculator;
        this.factory = factory;
    }
    */

    @Override
    public Collection<Derivative> parseDerivatives(HtmlPage page) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<Derivative> parseDerivatives(HtmlPage page, Stock parent) {
        throw new NotImplementedException();
    }

    @Override
    public Tuple<Collection<Derivative>> parseDerivativesTuple(HtmlPage page) {
        return parseDerivativesTuple(page,null);
    }

    @Override
    public Tuple<Collection<Derivative>> parseDerivativesTuple(HtmlPage htmlPage, StockPrice stockPrice) {
        throw new oahu.exceptions.NotImplementedException();
    }

    @Override
    public StockPrice parseSpot(HtmlPage htmlPage, String s) {
        throw new oahu.exceptions.NotImplementedException();
    }

    @Override
    public Map<String, StockPrice> parseSpots(HtmlPage page) {
        throw new oahu.exceptions.NotImplementedException();
    }
}
