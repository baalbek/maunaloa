package maunaloa.events.mongodb;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 21.10.13
 * Time: 13:05
 */
public class SaveToMongoDBEvent {
    private final int location;

    public SaveToMongoDBEvent(int location) {
        this.location = location;
    }

    public int getLocation() {
        return location;
    }
}
