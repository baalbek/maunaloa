package maunaloa.models;

import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import maunaloa.domain.MongoDBResult;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public interface ChartWindowDressingModel {
    WriteResult updateCoord(ObjectId id, DBObject p1, DBObject p2);
    MongoDBResult saveFibonacci(String ticker, int location, DBObject p1, DBObject p2);
    List<DBObject> fetchFibonacci(String ticker, int location, Date fromDate, Date toDate);
}


