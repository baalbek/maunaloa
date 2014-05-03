package maunaloa.entities.windowdressing;

import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 03.05.14
 * Time: 12:48
 */
public abstract class AbstractWindowDressingItem {
    protected ObjectId oid;
    protected final String ticker;
    protected final int location;

    public AbstractWindowDressingItem(String ticker,
                                      int location) {
        this.ticker = ticker;
        this.location = location;
    }
    public ObjectId getOid() {
        return oid;
    }
    public void setOid(ObjectId oid) {
        this.oid = oid;
    }

    public String getTicker() {
        return ticker;
    }

    public int getLocation() {
        return location;
    }
}
