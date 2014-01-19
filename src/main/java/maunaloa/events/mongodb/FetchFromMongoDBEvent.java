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
    private final List<DBObject> fibonacci;
    private final List<DBObject> levels;

    public FetchFromMongoDBEvent(List<DBObject> fibonacci,
                                 List<DBObject> levels) {
        this.fibonacci = fibonacci;
        this.levels = levels;
    }

    public List<DBObject> getFibonacci() {
        return fibonacci;
    }
    public List<DBObject> getLevels() {
        return levels;
    }
}
