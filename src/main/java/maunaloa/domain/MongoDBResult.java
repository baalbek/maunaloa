package maunaloa.domain;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/2/13
 * Time: 10:45 PM
 */
public class MongoDBResult {
    private final WriteResult writeResult;
    private final BasicDBObject savedObject;
    public MongoDBResult(BasicDBObject savedObject, WriteResult writeResult) {
        this.savedObject = savedObject;
        this.writeResult = writeResult;
    }

    public WriteResult getWriteResult() {
        return writeResult;
    }

    public BasicDBObject getSavedObject() {
        return savedObject;
    }
    public boolean isOk() {
        return writeResult.getLastError().ok();
    }
    public ObjectId getObjectId() {
        return (ObjectId)savedObject.get("_id");
    }
}
