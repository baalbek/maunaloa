package maunaloa.views.charts;

import com.mongodb.DBObject;

import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 23.04.14
 * Time: 10:15
 */
public class FinancialCoord {
    //region Properties
    private final LocalDate x;
    private final double y;

    public LocalDate getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    //endregion Properties

    //region Create

    public FinancialCoord(LocalDate x, double y) {
        this.x = x;
        this.y = y;
    }

    public static FinancialCoord create(DBObject obj) {
        double y = (Double)obj.get("y");
        LocalDate x = (LocalDate)obj.get("x");
        return new FinancialCoord(x,y);
    }

    //endregion Create
}
