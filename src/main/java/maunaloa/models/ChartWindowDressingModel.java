package maunaloa.models;

import com.mongodb.DBObject;
import maunaloa.domain.MongoDBResult;

import java.util.Date;
import java.util.List;

public interface ChartWindowDressingModel {
    MongoDBResult saveFibonacci(String ticker, int location, DBObject p1, DBObject p2);
    List<DBObject> fetchFibonacci(String ticker, Date fromDate, Date toDate);
}


