package maunaloa.models;

import com.mongodb.BasicDBObject;
import maunaloa.domain.MongoDBResult;

import java.util.Date;
import java.util.List;

public interface ChartWindowDressingModel {
    MongoDBResult saveFibonacci(String ticker, int location, BasicDBObject p1, BasicDBObject p2);
    List<BasicDBObject> fetchFibonacci(String ticker, Date fromDate, Date toDate);
}


