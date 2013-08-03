package maunaloa.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 8/3/13
 * Time: 1:25 PM
 */
@Aspect
public class MemoizeAspect {
    Logger log = Logger.getLogger(getClass().getPackage().getName());
    private HashMap<String,Object>  cache = new HashMap<>();

    @Pointcut("@annotation(oahu.annotations.Memoize)")
    public void memoizePointcut() {
    }


    @Around("memoizePointcut() && args(s1)")
    public Object memoizeString1(ProceedingJoinPoint jp, String s1) throws Throwable {

        if (cache.containsKey(s1)) {
            log.info(String.format("Cache hit: %s" ,s1));
            return cache.get(s1);
        }
        else  {
            log.info(String.format("Memoized: %s" ,s1));
            Object result = jp.proceed();
            cache.put(s1,result);
            return result;
        }
    }
}
