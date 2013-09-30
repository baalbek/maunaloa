package maunaloa.views;

import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 9/30/13
 * Time: 11:19 AM
 */
public interface MongodbLine {
    public static int P1 = 1;
    public static int P2 = 2;
    ObjectId getMongodbId();
    boolean getActive();
    void setActive(boolean value);
    MongodbCoord coord(int pt);
}
