package maunaloa.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 1/19/13
 * Time: 12:09 AM
 */
@Aspect
public class TraceAspect {
    Logger log = Logger.getLogger(getClass().getPackage().getName());

    @Pointcut("execution(* maunaloa..*(..)) && !execution(* maunaloa.controllers..*(..))")
    public void maunaloaTracePointcut() {
    }

    @Pointcut("execution(* oahu..*(..)) && !execution(* oahu.controllers.ChartViewModel.*(..))")
    public void oahuTracePointcut() {
    }

    @Pointcut("execution(* maunakea..*(..))")
    public void maunakeaTracePointcut() {
    }

    @Around("maunaloaTracePointcut()")
    public Object traceMaunloa(ProceedingJoinPoint jp) throws Throwable {
        return traceAndReturn(jp);
    }

    @Around("oahuTracePointcut()")
    public Object traceOahu(ProceedingJoinPoint jp) throws Throwable {
        return traceAndReturn(jp);
    }

    @Around("maunakeaTracePointcut()")
    public Object traceMaunakea(ProceedingJoinPoint jp) throws Throwable {
        return traceAndReturn(jp);
    }

    private Object traceAndReturn(ProceedingJoinPoint jp) throws Throwable {
        StringBuilder sb = new StringBuilder(jp.toString());
        sb.append("\n\t------------------------------------");
        sb.append("\n\tArgs:");
        Object[] args = jp.getArgs();

        for (int i = 0; i < args.length; ++i) {
            sb.append("\n\t").append(args[i]);
        }

        Object result = null;

        try {
            result = jp.proceed();
            sb.append("\n\n\t------------------------------------");
            sb.append("\n\tResult:").append(result);
            sb.append("\n\t------------------------------------");
            log.debug(sb.toString());
        }
        catch (Exception ex) {
            log.warn(String.format("%s: %s", jp,ex.toString()));
        }


        return result;
    }
}
