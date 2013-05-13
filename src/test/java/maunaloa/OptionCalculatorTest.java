package maunaloa;

import maunaloa.models.impl.BlackScholesCalculator;
import maunaloa.utils.DateUtils;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class OptionCalculatorTest {
    public OptionCalculatorTest() {

    }

    @Test
    public void testBlackScholesCalculator() {
        final XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("/test-maunaloa.xml"));

        BlackScholesCalculator calc = (BlackScholesCalculator) factory.getBean("calculator");
        assertNotNull(calc);

        StockBean sbean = new StockBean(100,120,90,98,1000);
        sbean.setDx(DateUtils.createDate(2013,1,1));
        CalculatedDerivativeBean bean = new CalculatedDerivativeBean("TEST",
                DerivativeBean.CALL,
                100,
                10,
                12,
                DateUtils.createDate(2013,6,1),
                sbean,
                calc);

        assertEquals(0.49,bean.getIvSell(),0.1);

        //System.out.println("Days: " + bean.daysProperty().get() + ", Iv sell: " + bean.getIvSell());

        //assertEquals(0.49,bean.getDelta(),0.1);
        System.out.println("Delta: " + bean.getDelta());
        System.out.println("Breakeven: " + bean.getBreakeven());
        System.out.println("Spread: " + bean.getSpread());
    }
}