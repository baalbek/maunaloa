package maunaloa.events;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/5/13
 * Time: 4:25 PM
 */
public interface MongoDBControllerListener {
    void onFetchedFromMongoDB(FetchedFromMongoDBEvent event);
}
