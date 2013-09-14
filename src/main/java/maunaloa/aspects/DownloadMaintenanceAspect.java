package maunaloa.aspects;

import maunakea.aspects.AbstractDownloadManagerAspect;
import oahu.annotations.StoreHtmlPage;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 5/10/13
 * Time: 10:20 AM
 */

@Aspect
public class DownloadMaintenanceAspect extends AbstractDownloadManagerAspect {

    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    private String storeDir;

    //region Overridden Methods
    @Override
    protected String fileNameFor(ProceedingJoinPoint proceedingJoinPoint, StoreHtmlPage storeHtmlPage) {
        Object[] args = proceedingJoinPoint.getArgs();
        return String.format("%s/%s.%s", getStoreDir(), args[0], "html");
    }
    //endregion Overridden Methods

    //region Properties
    public String getStoreDir() {
        return storeDir;
    }

    public void setStoreDir(String storeDir) {
        this.storeDir = storeDir;
    }

    //endregion Properties

}
