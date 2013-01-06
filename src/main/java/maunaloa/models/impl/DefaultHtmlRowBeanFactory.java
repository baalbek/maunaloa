package maunaloa.models.impl;

import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import maunakea.util.Conversions;
import oahu.financial.HtmlRowBeanFactory;
import oahu.financial.OptionCalculator;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/6/13
 * Time: 10:46 PM
 */
public class DefaultHtmlRowBeanFactory implements HtmlRowBeanFactory {
    static int NAME = 0;
    static int OPTYPE = 1;
    static int X = 2;
    static int EXPIRY = 3;
    static int BUY = 4;
    static int SELL = 5;


    @Override
    public DerivativeBean create(List<HtmlTableCell> cells, StockBean parent, OptionCalculator calculator) {
        int curOpType = parseOpType(cells, OPTYPE);
        if (curOpType == DerivativeBean.OPTYPE_UNDEF) {
            return null;
        }

        return new DerivativeBean(Conversions.c2s(cells, NAME),
                curOpType,
                Conversions.c2f(cells, X),
                Conversions.c2f(cells, BUY),
                Conversions.c2f(cells, SELL),
                Conversions.c2d(cells, EXPIRY),
                parent);
    }

    private static int parseOpType(List<HtmlTableCell> cells, int index) {
        String tmp = cells.get(index).asText();

        if ("American call".equals(tmp)) {
            return DerivativeBean.CALL;
        } else if ("American put".equals(tmp)) {
            return DerivativeBean.PUT;
        } else {
            return DerivativeBean.OPTYPE_UNDEF;
        }
    }
}
