package maunaloa.events.mongodb;

import com.mongodb.DBObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 21.10.13
 * Time: 13:06
 */
public class FetchFromMongoDBEvent {
    private final List<DBObject> lines;

    public FetchFromMongoDBEvent(List<DBObject> lines) {
        this.lines = lines;
    }

    public List<DBObject> getLines() {
        return lines;
    }
}
