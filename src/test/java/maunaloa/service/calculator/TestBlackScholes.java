package maunaloa.service.calculator;

import maunakea.financial.beans.CalculatedDerivativeBean;
import oahu.financial.OptionCalculator;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
import org.joda.time.DateMidnight;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

import static org.junit.Assert.assertEquals;

//import org.junit.Before;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 5/22/13
 * Time: 9:19 AM
 */
public class TestBlackScholes {

    private StockBean createStockBean(double spot) {

        return new StockBean(new Date(),0.0,0.0,0.0,spot,0);
    }
    /*
  asusupposfsddsfsd  @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    //*/

    private OptionCalculator getCalculator() {
        ApplicationContext factory = new ClassPathXmlApplicationContext("test-maunaloa.xml");
        return factory.getBean("calculator",OptionCalculator.class);
    }

    private CalculatedDerivativeBean createDerivativeBean(StockBean parent,
                                                          int opType,
                                                          double x,
                                                          double buy,
                                                          double sell,
                                                          OptionCalculator calc) {
        Date dx = new DateMidnight().plusDays(183).toDate();

        return new CalculatedDerivativeBean(String.format("Test %d",opType),
                                            opType,
                                            x,
                                            buy,
                                            sell,
                                            dx,
                                            parent,
                                            calc);
    }

    @Test
    public void testDelta() {
        OptionCalculator calculator = getCalculator();

        CalculatedDerivativeBean bean =
        createDerivativeBean(createStockBean(100),
                             DerivativeBean.CALL,
                             100,
                             12,
                             14.5,
                             calculator);
        assertEquals("Call delta 1",0.6,bean.getDelta(),0.01);
    }

    @Test
    public void testStockPriceFor() {
        OptionCalculator calculator = getCalculator();

        CalculatedDerivativeBean bean =
                createDerivativeBean(createStockBean(100),
                        DerivativeBean.CALL,
                        100,
                        12,
                        14.5,
                        calculator);

        System.out.println(bean.getIvBuy());
        assertEquals("Stock price for call price 1",103.1,calculator.stockPriceFor(14,bean),0.1);
    }
}
