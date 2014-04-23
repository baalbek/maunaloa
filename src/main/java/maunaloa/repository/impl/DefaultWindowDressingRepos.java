package maunaloa.repository.impl;

import com.mongodb.*;
import maunaloa.controllers.ControllerHub;
import maunaloa.entities.windowdressing.FibLine;
import maunaloa.repository.WindowDressingRepository;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by rcs on 4/22/14.
 */
public class DefaultWindowDressingRepos implements WindowDressingRepository {
    //region Private Stuff
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    private Map<String,List<FibLine>> fibLines;
    private String makeKey(String ticker, int location) {
        return String.format("%s:%d", ticker, location);
    }

    private DB _cloudConnection;
    private DB cloudConnection() throws UnknownHostException {
        /*(let [clt (MongoClient. "paulo.mongohq.com" 10044)
        db (.getDB clt "app19368679")
        auth-result (.authenticate db "heroku" (.toCharArray "4a0b28228851cfa6ef121ee73560dd58"))]
        */
        if (_cloudConnection == null) {
            MongoClient client = new MongoClient("paulo.mongohq.com", 10044);
            _cloudConnection = client.getDB("app19368679");
            boolean authResult = _cloudConnection.authenticate("heroku","4a0b28228851cfa6ef121ee73560dd58".toCharArray());
            log.info(String.format("Connected to: paulo.mongohq.com, user: heroku, with result: %s", authResult));
        }
        return _cloudConnection;
    }
    private DB _localConnection;
    private DB localConnection() throws UnknownHostException {
        /*(let [clt (MongoClient. host 27017)
        db (.getDB clt "maunaloa")]
        (LOG/info (str "Connected to: " host ", database: maunaloa"))
         */
        if (_localConnection == null) {
            MongoClient client = new MongoClient(mongodbHost, 27017);
            _localConnection = client.getDB("maunaloa");
            log.info(String.format("Connected to: %s, database: maunaloa", mongodbHost));
        }

        return _localConnection;
    }
    private DB getConnection() throws UnknownHostException {
        return isCloud ? cloudConnection() : localConnection();
    }
    //endregion Private Stuff

    //region Interface WindowDressingRepository
    @Override
    public List<FibLine> fetchFibLines(String ticker, int location, int status) {
        if (fibLines == null) {
            fibLines = new HashMap<>();
        }
        String key = makeKey(ticker,location);
        List<FibLine> result = null;
        if (fibLines.containsKey(key)) {
            result = fibLines.get(key);
        }
        else {
            try {
                Function<DBObject,FibLine> mongo2fibline = o -> {
                    /*{ "_id" : ObjectId("531877ed389056a05cd67f79"),
                    "tix" : "OSEBX",
                    "active" : true,
                    "loc" : NumberLong(4),
                    "p1" : { "x" : ISODate("2013-06-27T22:00:00Z"), "y" : 456.1936726241462 },
                    "p2" : { "x" : ISODate("2014-01-16T23:00:00Z"), "y" : 563.0399627169718 }
                    */
                    /*
                    int loc =
                    IRuler vruler = hub.getVruler(loc);
                    IRuler hruler = hub.getVruler(loc);
                     */
                    return null; //new FibLine();
                };
                DBCollection collection = getConnection().getCollection("fibonacci");
                BasicDBObject query = new BasicDBObject("tix",ticker);
                query.append("loc",location);
                query.append("active",true);
                DBCursor cursor = collection.find(query);
                List<DBObject> objs = cursor.toArray();
                result = objs.stream().map(mongo2fibline).collect(Collectors.toList());
            } catch (UnknownHostException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return result;
    }

    private ControllerHub hub;
    @Override
    public void setControllerHub(ControllerHub hub) {
        this.hub = hub;
    }
    //endregion Interface WindowDressingRepository


    //region Properties

    private boolean isCloud = false;

    public boolean isCloud() {
        return isCloud;
    }
    public void setCloud(boolean isCloud) {
        this.isCloud = isCloud;
    }

    private String mongodbHost;

    public String getMongodbHost() {
        return mongodbHost;
    }

    public void setMongodbHost(String mongodbHost) {
        this.mongodbHost = mongodbHost;
    }

    //endregion Properties
}
