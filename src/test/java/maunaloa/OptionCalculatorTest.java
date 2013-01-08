package maunaloa;

import kalihiwai.financial.OptionCalculator;
import maunaloa.beans.CalculatedDerivativeBean;
import maunaloa.models.impl.BlackScholesCalculator;
import maunaloa.utils.DateUtils;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.net.URL;
import java.util.Date;

public class OptionCalculatorTest {
    public OptionCalculatorTest() {

    }

    @Test
    public void testBlackScholesCalculator() {
        final XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("/test-maunaloa.xml"));

        BlackScholesCalculator calc = (BlackScholesCalculator) factory.getBean("kalihiwai-calc");
        assertNotNull(calc);

        StockBean sbean = new StockBean(100,120,90,98,1000);
        sbean.setDx(new Date());
        CalculatedDerivativeBean bean = new CalculatedDerivativeBean("TEST",
                DerivativeBean.CALL,
                100,
                10,
                12,
                DateUtils.createDate(2013,3,1),
                sbean,
                calc);

    }
}