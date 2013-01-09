package maunaloa.models.impl;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import maunaloa.beans.CalculatedDerivativeBean;
import oahu.beans.Tuple;
import oahu.financial.EtradeDerivativeParser;
import oahu.financial.HtmlRowBeanFactory;
import oahu.financial.OptionCalculator;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/10/13
 * Time: 12:43 AM
 */
public class FilteredDerivativeParser implements EtradeDerivativeParser {
    private final OptionCalculator calculator;
    private final HtmlRowBeanFactory factory;



    public FilteredDerivativeParser(OptionCalculator calculator,
                                   HtmlRowBeanFactory factory) {
        this.calculator = calculator;
        this.factory = factory;
    }

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
        Collection<DerivativeBean> calls = new ArrayList<>();
        Collection<DerivativeBean> puts = new ArrayList<>();

        HtmlTable table = page.getFirstByXPath("//table[@class='com topmargin']");

        for (final HtmlTableRow row : table.getRows()) {
            List<HtmlTableCell> cells = row.getCells();
            if (cells.size() == 9) {

                //DerivativeBean d = FinancialUtils.derivativeBeanFromHtml(cells, parent, calculator);

                DerivativeBean d = factory.create(cells, parent, calculator);



                //System.out.println("derivative bean: " + d);


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
    }
}
