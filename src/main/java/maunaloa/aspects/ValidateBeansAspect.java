package maunaloa.aspects;

import maunakea.financial.beans.CalculatedDerivativeBean;
import oahu.financial.beans.DerivativeBean;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 5/2/13
 * Time: 1:34 PM
 */

@Aspect
public class ValidateBeansAspect {
    Logger log = Logger.getLogger(getClass().getPackage().getName());

    @Pointcut("execution(* oahu.financial.Etrade.getCalls(String))")
    public void getCallsPointcut() {
    }


    @Pointcut("execution(* oahu.financial.Etrade.getPuts(String))")
    public void getPutsPointcut() {
    }



    @Around("getPutsPointcut()")
    public Collection<DerivativeBean> getPutsPointcutMethod(ProceedingJoinPoint jp) throws Throwable {

        Collection<DerivativeBean> tmp = (Collection<DerivativeBean>)jp.proceed();

        Collection<DerivativeBean> result = new ArrayList<>();

        log.info(String.format("%s\nNumber of puts: %d",jp.toString(),tmp.size()));

        for (DerivativeBean bean : tmp) {
            CalculatedDerivativeBean cb = (CalculatedDerivativeBean)bean;

            if (isOk(cb) == false) continue;

            result.add(cb);
        }

        return result;
    }

    @Around("getCallsPointcut()")
    public Collection<DerivativeBean> getCallsPointcutMethod(ProceedingJoinPoint jp) throws Throwable {

        Collection<DerivativeBean> tmp = (Collection<DerivativeBean>)jp.proceed();

        Collection<DerivativeBean> result = new ArrayList<>();

        log.info(String.format("%s\nNumber of calls: %d",jp.toString(),tmp.size()));

        for (DerivativeBean bean : tmp) {
            CalculatedDerivativeBean cb = (CalculatedDerivativeBean)bean;

            if (isOk(cb) == false) continue;

            result.add(cb);
        }

        return result;
    }

    private boolean isOk(CalculatedDerivativeBean cb) {
        String ticker = cb.getTicker();

        if (cb.getParent() == null) {
            log.warn(String.format("%s: parent is null",ticker));
            return false;
        }


        if (cb.daysProperty().get() < 0) {
            log.info(String.format("%s has expired",ticker));
            return false;
        }

        if (cb.buyProperty().get() <= 0) {
            log.info(String.format("%s: buy <= 0.0",ticker));
            return false;
        }


        if (cb.sellProperty().get() <= 0) {
            log.info(String.format("%s: sell <= 0.0",ticker));
            return false;
        }

        if (cb.getIvSell() < 0 || cb.getIvBuy() < 0) {
            log.info(String.format("%s: iv not valid",ticker));
            return false;
        }
        return true;
    }


    /*
    @Pointcut("execution(* oahu.financial.Etrade.getSpot(String))")
    public void getSpotPointcut() {
    }
    */
}
