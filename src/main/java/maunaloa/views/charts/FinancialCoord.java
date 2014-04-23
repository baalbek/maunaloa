package maunaloa.views.charts;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 23.04.14
 * Time: 10:15
 */
public class FinancialCoord {
    //region Properties
    private Date x;
    private double y;
    //endregion Properties

    public FinancialCoord(Date x, double y) {
        this.x = x;
        this.y = y;
    }
}
