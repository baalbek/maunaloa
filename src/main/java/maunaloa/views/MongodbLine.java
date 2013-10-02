package maunaloa.views;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 9/30/13
 * Time: 11:19 AM
 */
public interface MongodbLine {
    static int P1 = 1;
    static int P2 = 2;
    static int LOC_CNDL = 1;
    static int LOC_WEEKS = 2;
    ObjectId getMongodbId();
    void setMongodbId(ObjectId id);
    boolean getActive();
    void setActive(boolean value);
    BasicDBObject coord(int pt);
    long getLocation();
}
