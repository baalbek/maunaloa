package maunaloa.events;

import com.mongodb.DBObject;
import oahu.financial.Stock;

import java.util.List;

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
    private final List<DBObject> lines;

    public MongoDBEvent(int location, int action) {
        this(location,action,null);
    }

    public MongoDBEvent(int location, int action, List<DBObject> lines) {
        this.location = location;
        this.action = action;
        this.lines = lines;
    }

    public int getLocation() {
        return location;
    }

    public int getAction() {
        return action;
    }
}
