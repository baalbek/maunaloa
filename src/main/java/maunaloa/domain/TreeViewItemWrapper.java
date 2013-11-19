package maunaloa.domain;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import maunaloa.views.MongodbLine;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 11/14/13
 * Time: 9:32 PM
 */
public class TreeViewItemWrapper {
    private final Object wrapped;

    public TreeViewItemWrapper (Object wrapped) {
        this.wrapped = wrapped;
    }
    @Override
    public String toString() {
        if (wrapped instanceof DBObject) {
            DBObject commObj = (DBObject)wrapped;
            String comment = (String)commObj.get("c");
            Date commentDate = (Date)commObj.get("dx");
            //return (String)((DBObject) wrapped).get("c");
            return String.format("%s - %s", commentDate,comment);
        }
        else if (wrapped instanceof MongodbLine) {
            MongodbLine line = (MongodbLine)wrapped;
            BasicDBObject p1 = line.coord(MongodbLine.P1);
            BasicDBObject p2 = line.coord(MongodbLine.P2);

            return String.format("x1: %s, y1: %.1f, x2: %s, y2: %.1f",
                    p1.get("x"),
                    p1.get("y"),
                    p2.get("x"),
                    p2.get("y"));
        }
        else {
            return wrapped.toString();
        }
    }

    public static TreeViewItemWrapper createDefault(String desc) {
        return new TreeViewItemWrapper (desc);
    }

    public Object getWrapped() {
        return wrapped;
    }
}
