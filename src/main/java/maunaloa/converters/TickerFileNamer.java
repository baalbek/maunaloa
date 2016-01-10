package maunaloa.converters;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * Created by rcs on 10.01.16.
 *
 */
public class TickerFileNamer implements Function<String,String> {
    private LocalDate downloadDate;

    @Override
    public String apply(String ticker) {
        return String.format("%d/%d/%d/%s.html",
                downloadDate.getYear(),
                downloadDate.getMonth().getValue(),
                downloadDate.getDayOfMonth(),
                ticker);
    }

    public void setDownloadDate(LocalDate date) {
        downloadDate = date;
    }
}
