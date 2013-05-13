package maunaloa.models.impl;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import oahu.beans.Tuple;
import oahu.financial.EtradeHtmlParser;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
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
    public Collection<DerivativeBean> parseDerivatives(HtmlPage page) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<DerivativeBean> parseDerivatives(HtmlPage page, StockBean parent) {
        throw new NotImplementedException();
    }

    @Override
    public Tuple<Collection<DerivativeBean>> parseDerivativesTuple(HtmlPage page) {
        return parseDerivativesTuple(page,null);
    }

    @Override
    public Tuple<Collection<DerivativeBean>> parseDerivativesTuple(HtmlPage page, StockBean parent) {
        return null;
        /*
        Collection<DerivativeBean> calls = new ArrayList<>();
        Collection<DerivativeBean> puts = new ArrayList<>();

        HtmlTable table = page.getFirstByXPath("//table[@class='com topmargin']");

        for (final HtmlTableRow row : table.getRows()) {
            List<HtmlTableCell> cells = row.getCells();
            if (cells.size() == 9) {

                DerivativeBean d = factory.create(cells, parent, calculator);

                if (d != null) {
                    CalculatedDerivativeBean cd = (CalculatedDerivativeBean)d;

                    if (cd.isCalculable() == false)  continue;

                    if (cd.getIvSell() < 0.0) continue;

                    //System.out.println("\toptype: " + d.getOpType());
                    if (d.getOpType() == DerivativeBean.CALL) {
                        calls.add(d);
                    } else if (d.getOpType() == DerivativeBean.PUT) {
                        puts.add(d);
                    }
                }
            }
        }

        return  new Tuple<>(calls,puts);
        */
    }

    @Override
    public StockBean parseSpot(HtmlPage page) {
        throw new oahu.exceptions.NotImplementedException();
    }

    @Override
    public Map<String, StockBean> parseSpots(HtmlPage page) {
        throw new oahu.exceptions.NotImplementedException();
    }
}
