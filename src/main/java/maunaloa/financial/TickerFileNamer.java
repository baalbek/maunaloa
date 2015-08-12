package maunaloa.financial;

import oahu.functional.Func2;

import java.time.LocalDate;

/**
 * Created by rcs on 5/27/14.
 */

public class TickerFileNamer implements Func2<String,LocalDate,String> {
    @Override
    public String apply(String ticker, LocalDate localDate) {
        return String.format("/%d/%d/%d/%s.html", 
                localDate.getYear(),
                localDate.getMonth().getValue(),
                localDate.getDayOfMonth(),
                ticker);
    }
}
