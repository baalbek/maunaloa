package maunaloa.aspects;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import oahu.annotations.StoreHtmlPage;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 5/10/13
 * Time: 10:20 AM
 */

@Aspect
public class DownloadMaintenanceAspect  {

    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    private String storeDir;

    @Pointcut("execution(@oahu.annotations.StoreHtmlPage * *(..)) && @annotation(annot)")
    public void storeHtmlPagePointcut(StoreHtmlPage annot) {
    }

    @Around("storeHtmlPagePointcut(annot)")
    public Object store2htmlPointcutMethod(ProceedingJoinPoint jp, StoreHtmlPage annot) throws Throwable {
        Object result = jp.proceed();

        Object[] args = jp.getArgs();

        LocalDateTime t = LocalDateTime.now();


        String htmlName = String.format("%s/%s-%d_%d_%d-%d_%d.html", storeDir,
                                        args[0],
                                        t.getYear(),t.getMonthValue(),t.getDayOfMonth(),t.getHour(),t.getMinute());

        HtmlPage pg = (HtmlPage)result;

        log.info(String.format("Downloaded: %s, saving to: %s", pg.getUrl(), htmlName));

        Files.write(Paths.get(htmlName), pg.getWebResponse().getContentAsString().getBytes());

        return result;
    }

    //region Properties
    public String getStoreDir() {
        return storeDir;
    }

    public void setStoreDir(String storeDir) {
        this.storeDir = storeDir;
    }

    //endregion Properties

}
