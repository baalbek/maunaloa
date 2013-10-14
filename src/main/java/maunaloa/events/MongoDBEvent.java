package maunaloa.events;

import oahu.financial.Stock;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/5/13
 * Time: 4:30 PM
 */
public class MongoDBEvent {
    public final static int SAVE_TO_DATASTORE  = 1;
    public final static int FETCH_FROM_DATASTORE  = 2;
    private final int location;
    private final int action;

    public MongoDBEvent(int location, int action) {
        this.location = location;
        this.action = action;
    }

    public int getLocation() {
        return location;
    }

    public int getAction() {
        return action;
    }
}
