package maunaloa.financial;

import java.util.function.Function;

/**
 * Created by rcs on 5/27/14.
 */

public class TickerFileNamer implements Function<String,String> {
    @Override
    public String apply(String ticker) {
        return String.format("%s.html", ticker);
    }
}
