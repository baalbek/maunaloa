package maunaloa.events;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 21.11.13
 * Time: 10:06
 */
public class NewLevelEvent {
    private final int location;
    private final double value;
    public NewLevelEvent(int location, double value) {
        this.location = location;
        this.value = value;
    }

    public int getLocation() {
        return location;
    }

    public double getValue() {
        return value;
    }
}
