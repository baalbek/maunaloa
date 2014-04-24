package maunaloa.views.charts;

import com.mongodb.DBObject;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 23.04.14
 * Time: 10:15
 */
public class FinancialCoord {
    //region Properties
    private final Date x;
    private final double y;

    public Date getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    //endregion Properties

    //region Create

    public FinancialCoord(Date x, double y) {
        this.x = x;
        this.y = y;
    }

    public static FinancialCoord create(DBObject obj) {
        return null;
    }

    //endregion Create
}
