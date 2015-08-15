package maunaloa.financial;

import maunaloa.controllers.ControllerHubListener;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * Created by rcs on 5/27/14.
 */

public class TickerFileNamer implements Function<String,String>, ControllerHubListener {

    private LocalDate downloadDate;

    @Override
    public String apply(String ticker) {
        return String.format("%d/%d/%d/%s.html",
                downloadDate.getYear(),
                downloadDate.getMonth().getValue(),
                downloadDate.getDayOfMonth(),
                ticker);
    }

    @Override
    public void setDownloadDate(LocalDate date) {
        downloadDate = date;
    }

    /*
    @Override
    public String apply(String ticker, LocalDate localDate) {
        return String.format("/%d/%d/%d/%s.html", 
                localDate.getYear(),
                localDate.getMonth().getValue(),
                localDate.getDayOfMonth(),
                ticker);
    }
    //*/
}
