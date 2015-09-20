package maunaloa.aspects;

import oahu.dto.Tuple3;
import oahu.exceptions.BinarySearchException;
import oahu.financial.DerivativePrice;
import oahu.financial.StockPrice;
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

    private Double spreadLimit = null;
    private Integer daysLimit = 0;


    //region Pointcuts
    @Pointcut("execution(* oahu.financial.repository.EtradeDerivatives.getCalls(String))")
    public void getCallsPointcut() {
    }


    @Pointcut("execution(* oahu.financial.repository.EtradeDerivatives.getPuts(String))")
    public void getPutsPointcut() {
    }

    @Pointcut("execution(* oahu.financial.repository.EtradeDerivatives.getSpotCallsPuts(String))")
    public void getSpotCallsPutsPointcut() {
    }

    @Around("getSpotCallsPutsPointcut()")
    public Tuple3<StockPrice,Collection<DerivativePrice>,Collection<DerivativePrice>>
    getSpotCallsPutsPointcutMethod(ProceedingJoinPoint jp) throws Throwable {
        Tuple3<StockPrice,Collection<DerivativePrice>,Collection<DerivativePrice>> tmp
                =(Tuple3<StockPrice,Collection<DerivativePrice>,Collection<DerivativePrice>> )jp.proceed();

        Collection<DerivativePrice> validatedCalls = validateItems(tmp.second());
        Collection<DerivativePrice> validatedPuts =  validateItems(tmp.third());

        return new Tuple3<>(tmp.first(),validatedCalls,validatedPuts);
    }

    @Around("getPutsPointcut()")
    public Collection<DerivativePrice> getPutsPointcutMethod(ProceedingJoinPoint jp) throws Throwable {
        Collection<DerivativePrice> tmp = (Collection<DerivativePrice>)jp.proceed();
        log.info(String.format("%s\nNumber of puts: %d",jp.toString(),tmp.size()));
        return validateItems(tmp);

    }

    @Around("getCallsPointcut()")
    public Collection<DerivativePrice> getCallsPointcutMethod(ProceedingJoinPoint jp) throws Throwable {
        Collection<DerivativePrice> tmp = (Collection<DerivativePrice>)jp.proceed();
        log.info(String.format("%s\nNumber of calls: %d",jp.toString(),tmp.size()));
        return validateItems(tmp);
    }
    //endregion Pointcuts

    //region Private Methods
    private Collection<DerivativePrice> validateItems(Collection<DerivativePrice> origItems) {
        Collection<DerivativePrice> result = new ArrayList<>();

        for (DerivativePrice bean : origItems) {

            if (isOk(bean) == false) continue;

            result.add(bean);
        }

        return result;
    }

    private boolean isOk(DerivativePrice cb) {
        String ticker = cb.getDerivative().getTicker();

        if (cb.getDerivative() == null) {
            log.warn(String.format("[isOk(DerivativePrice] %s: Derivative is null",ticker));
            return false;
        }

        if (cb.getStockPrice() == null) {
            log.warn(String.format("[isOk(DerivativePrice] %s: StockPrice is null",ticker));
            return false;
        }

        if (cb.getDays() < daysLimit) {
            log.info(String.format("[isOk(DerivativePrice] %s has expired within %d days",ticker,daysLimit));
            return false;
        }

        if (cb.getBuy()<= 0) {
            log.info(String.format("[isOk(DerivativePrice] %s: buy <= 0.0",ticker));
            return false;
        }

        if (cb.getSell() <= 0) {
            log.info(String.format("[isOk(DerivativePrice] %s: sell <= 0.0",ticker));
            return false;
        }

        if (spreadLimit != null) {
            double spread = cb.getSell() - cb.getBuy();
            if (spread > spreadLimit.doubleValue()) {
                log.info(String.format("[isOk(DerivativePrice] %s: spread (%.2f) larger than allowed (%.2f)",ticker,spread,spreadLimit));
                return false;
            }
        }

        try {
            if (cb.getIvSell() <= 0) {
                log.info(String.format("[isOk(DerivativePrice] %s: ivSell <= 0.0",ticker));
                return false;
            }

            if (cb.getIvBuy() <= 0) {
                log.info(String.format("[isOk(DerivativePrice] %s: ivBuy <= 0.0",ticker));
                return false;
            }
        }
        catch (BinarySearchException ex) {
            log.warn(String.format("%s: %s",ticker,ex.getMessage()));
            return false;
        }

        return true;
    }
    //endregion Private Methods

    //region Properties
    public Double getSpreadLimit() {
        return spreadLimit;
    }

    public void setSpreadLimit(Double spreadLimit) {
        this.spreadLimit = spreadLimit;
    }

    public Integer getDaysLimit() {
        return daysLimit;
    }

    public void setDaysLimit(Integer daysLimit) {
        this.daysLimit = daysLimit;
    }
    //endregion Properties


    /*
    @Pointcut("execution(* oahu.financial.Etrade.getSpot(String))")
    public void getSpotPointcut() {
    }
    */
}
