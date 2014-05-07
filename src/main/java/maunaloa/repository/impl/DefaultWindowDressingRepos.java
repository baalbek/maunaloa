package maunaloa.repository.impl;

import com.mongodb.*;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.entities.windowdressing.FibLineEntity;
import maunaloa.entities.windowdressing.LevelEntity;
import maunaloa.repository.WindowDressingRepository;
import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.FinancialCoord;
import oahu.domain.Tuple;
import oahux.chart.IRuler;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

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

    private Map<String,List<ChartItem>> fibLines;
    private HashMap<String, List<ChartItem>> levels;

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
    public List<ChartItem> fetchLevels(String ticker,
                                       int location,
                                       int status,
                                       IRuler vruler) {
        if (levels == null) {
            levels = new HashMap<>();
        }
        String key = makeKey(ticker,location);
        List<ChartItem> result = null;
        if (levels.containsKey(key)) {
            result = levels.get(key);
        }
        else {
            Function<DBObject,LevelEntity> mongo2level = o -> {
                /*{ "_id" : ObjectId("52f1494744aeff728437e6c9"),
                    "tix" : "STL",
                    "active" : true,
                    "loc" : NumberLong(1),
                    "value" : 140 }*/
                ObjectId oid = (ObjectId)o.get("_id");
                double level = (Double)o.get("value");
                return new LevelEntity(oid,ticker,location,level,vruler);
            };
            try {
                DBCollection collection = getConnection().getCollection("levels");
                BasicDBObject query = new BasicDBObject("tix",ticker);
                query.append("loc",location);
                query.append("active",true);
                DBCursor cursor = collection.find(query);
                List<DBObject> objs = cursor.toArray();
                result = objs.stream().map(mongo2level).collect(Collectors.toList());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<ChartItem> fetchFibLines(String ticker,
                                         int location,
                                         int status,
                                         Tuple<IRuler> rulers) {
        if (fibLines == null) {
            fibLines = new HashMap<>();
        }
        String key = makeKey(ticker,location);
        List<ChartItem> result = null;
        if (fibLines.containsKey(key)) {
            result = fibLines.get(key);
        }
        else {
            try {
                Function<DBObject,FibLineEntity> mongo2fibline = o -> {
                    /*{ "_id" : ObjectId("531877ed389056a05cd67f79"),
                    "tix" : "OSEBX",
                    "active" : true,
                    "loc" : NumberLong(4),
                    "p1" : { "x" : ISODate("2013-06-27T22:00:00Z"), "y" : 456.1936726241462 },
                    "p2" : { "x" : ISODate("2014-01-16T23:00:00Z"), "y" : 563.0399627169718 }
                    */
                    ObjectId oid = (ObjectId)o.get("_id");
                    FinancialCoord p1 = FinancialCoord.create((DBObject) o.get("p1"));
                    FinancialCoord p2 = FinancialCoord.create((DBObject)o.get("p2"));
                    return new FibLineEntity(oid,ticker,location,p1,p2,rulers);
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


    @Override
    public void saveOrUpdate(ChartItem item) {
        item.saveToRepos(this);
    }

    @Override
    public void saveFibonacci(FibLineEntity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveLevel(LevelEntity entity) {
        try {
            DBCollection coll = getConnection().getCollection("levels");

            Function<BasicDBObject,WriteResult> saveFn = o -> {
                BasicDBObject setObj = new BasicDBObject("$set", o);
                BasicDBObject query = new BasicDBObject("_id", entity.getOid());
                return coll.update(query, setObj);
            };

            MaunaloaStatus stat = entity.getStatus();
            WriteResult wr = null;
            switch (stat.getEntityStatus()) {
                case StatusCodes.ENTITY_NEW: {
                    BasicDBObject newEnt =
                            new BasicDBObject("tix", entity.getTicker()).
                            append("active", true).
                            append("loc", entity.getLocation()).
                            append("value", entity.getLevelValue());
                    wr = coll.save(newEnt);
                    entity.setOid((ObjectId)newEnt.get("_id"));
                    entity.setEntityStatus(StatusCodes.ENTITY_CLEAN);
                }
                break;
                case StatusCodes.ENTITY_DIRTY: {
                    wr = saveFn.apply(new BasicDBObject("value", entity.getLevelValue()));
                    entity.setEntityStatus(StatusCodes.ENTITY_CLEAN);
                }
                break;
                case StatusCodes.ENTITY_TO_BE_INACTIVE: {
                    wr = saveFn.apply(new BasicDBObject("active", false));
                    entity.setEntityStatus(StatusCodes.ENTITY_IS_INACTIVE);
                }
                break;
            }
            if (wr != null) {
                if (wr.getLastError().ok() == true) {
                    log.info(String.format("Saved ok for object with id: %s",entity.getOid()));
                }
                else {
                    log.error(String.format("Save failed with error: %s", wr.getError()));
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCloud() {
        return isCloud;
    }
    @Override
    public void setCloud(boolean isCloud) {
        this.isCloud = isCloud;
    }

    //endregion Interface WindowDressingRepository


    //region Properties

    private boolean isCloud = false;

    private String mongodbHost;

    public String getMongodbHost() {
        return mongodbHost;
    }

    public void setMongodbHost(String mongodbHost) {
        this.mongodbHost = mongodbHost;
    }

    //endregion Properties
}
