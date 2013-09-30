package maunaloa.views;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 9/30/13
 * Time: 11:26 AM
 */
public class MongodbCoord {
    private final Date date;
    private final double value;

    public MongodbCoord(Date date, double value) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }
}
