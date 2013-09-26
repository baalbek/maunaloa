package maunaloa.models;

import com.mongodb.BasicDBObject;

import java.util.Date;
import java.util.List;

public interface ChartWindowDressingModel {
    void saveFibonacci(String ticker, Date d0, double val0, Date d1, double val1);
    List<BasicDBObject> fetchFibonacci(String ticker, Date fromDate, Date toDate);
}


