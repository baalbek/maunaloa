package maunaloa.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/19/13
 * Time: 12:09 AM
 */
@Aspect
public class TraceAspect {
    Logger log = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* *.*(..))")
    public void logPointcut() {
    }

    @Before("logPointcut()")
    public void aroundControllerMethod(JoinPoint joinPoint) throws Throwable {
        log.info("Invoked: " + joinPoint);
        System.out.println("Invoced: " + joinPoint);
    }

    /*
    @Around("execution(* *.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Bah!");
        return joinPoint.proceed();
    }
    */
}
