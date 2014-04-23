package maunaloa.entities.windowdressing;

import maunaloa.views.charts.FinancialCoord;
import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 13:34
 */
public class FibLine {
    //region Properties
    private ObjectId oid;
    private String ticker;
    private int location;
    private FinancialCoord p1, p2;

    public ObjectId getOid() {
        return oid;
    }

    public String getTicker() {
        return ticker;
    }

    public int getLocation() {
        return location;
    }
    //endregion Properties

    //region Create
    public FibLine(String ticker,
                   int location,
                   FinancialCoord p1,
                   FinancialCoord p2) {
        this(null,ticker,location,p1,p2);
    }
    public FibLine(ObjectId oid,
                   String ticker,
                   int location,
                   FinancialCoord p1,
                   FinancialCoord p2) {
        this.oid = oid;
        this.ticker = ticker;
        this.location = location;
        this.p1 = p1;
        this.p2 = p2;
    }
    //endregion Create
}
