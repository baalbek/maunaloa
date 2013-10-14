package maunaloa.events;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/9/13
 * Time: 10:48 PM
 */
public interface MainFrameControllerListener {
    void onFibonacciEvent(FibonacciEvent event);
    void onMongoDBEvent(MongoDBEvent event);
}
