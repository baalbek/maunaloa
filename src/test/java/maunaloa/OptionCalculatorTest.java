package maunaloa;

import kalihiwai.financial.OptionCalculator;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.net.URL;

public class OptionCalculatorTest {
    public OptionCalculatorTest() {

    }

    @Test
    public void one() {
        System.out.println("Hi");
        final XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("/test-maunaloa.xml"));

        kalihiwai.financial.OptionCalculator calc = (OptionCalculator) factory.getBean("kalihiwai-calc");
        assertNotNull(calc);

    }
}