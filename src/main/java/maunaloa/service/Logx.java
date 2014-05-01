package maunaloa.service;

import org.apache.log4j.Logger;

import java.util.function.Supplier;

/**
 * Created by rcs on 5/1/14.
 */
public class Logx {
    public static void debug(Logger log, Supplier<String> s) {
        if (log.isDebugEnabled()) {
            log.debug(s.get());
        }
    }
}
