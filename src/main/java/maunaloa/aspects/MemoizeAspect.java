package maunaloa.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.security.MessageDigest;
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


    /*
    @Around("memoizePointcut() && args(s1,..)")
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
    //*/

    @Around("memoizePointcut()")
    public Object memoizePointCutMethod(ProceedingJoinPoint jp) throws Throwable {


        StringBuilder sb = new StringBuilder(jp.getThis().getClass().getName());
        sb.append(jp.getSignature().getName());

        Object[] args = jp.getArgs();

        for (int i = 0; i < args.length; ++i) {
            sb.append(":").append(args[i]);
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("Memoize key: %s", sb.toString()));
        }

        /*
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.reset();

        md.update(sb.toString().getBytes());
        String key = md.digest().toString();
        */

        String key = sb.toString();

        if (cache.containsKey(key)) {
            log.info(String.format("Cache hit: %s" ,key));
            return cache.get(key);
        }
        else  {
            log.info(String.format("Memoized: %s" ,key));
            Object result = jp.proceed();
            cache.put(key,result);
            return result;
        }
    }
}
