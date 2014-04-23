package maunaloa.repository.impl;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import maunaloa.entities.windowdressing.FibLine;
import maunaloa.repository.WindowDressingRepository;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rcs on 4/22/14.
 */
public class DefaultWindowDressingRepos implements WindowDressingRepository {
    //region Private Stuff
    /*{ "_id" : ObjectId("531877ed389056a05cd67f79"),
    "tix" : "OSEBX",
    "active" : true,
    "loc" : NumberLong(4),
    "p1" : { "x" : ISODate("2013-06-27T22:00:00Z"), "y" : 456.1936726241462 },
    "p2" : { "x" : ISODate("2014-01-16T23:00:00Z"), "y" : 563.0399627169718 }
    */
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    private Map<String,Collection<FibLine>> fibLines;
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
    public Collection<FibLine> fetchFibLines(String ticker, int location, int status) {
        if (fibLines == null) {
            fibLines = new HashMap<>();
        }
        String key = makeKey(ticker,location);
        Collection<FibLine> result = null;
        if (fibLines.containsKey(key)) {
            result = fibLines.get(key);
        }
        else {

        }
        return result;
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
