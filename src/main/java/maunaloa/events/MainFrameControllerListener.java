package maunaloa.events;

import maunaloa.events.mongodb.FetchFromMongoDBEvent;
import maunaloa.events.mongodb.SaveToMongoDBEvent;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/9/13
 * Time: 10:48 PM
 */
public interface MainFrameControllerListener {
    void onFibonacciEvent(FibonacciEvent event);
    void onFetchFromMongoDBEvent(FetchFromMongoDBEvent event);
    void onSaveToMongoDBEvent(SaveToMongoDBEvent event);
    void onNewLevelEvent(NewLevelEvent evt);
}
