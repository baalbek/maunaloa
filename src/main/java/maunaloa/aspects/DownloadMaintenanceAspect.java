package maunaloa.aspects;

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
public class DownloadMaintenanceAspect  {

    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    private String storeDir;

    //region Properties
    public String getStoreDir() {
        return storeDir;
    }

    public void setStoreDir(String storeDir) {
        this.storeDir = storeDir;
    }

    //endregion Properties

}
