package maunaloa.events;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/9/13
 * Time: 11:18 PM
 */
public class ChartCanvasLineEvent {
    public final static int NEW_LINE = 1;
    public final static int HODE_SEL_LINES = 2;
    public final static int HODE_ALL_LINES = 3;
    public final static int DELETE_SEL_LINES = 4;
    public final static int DELETE_ALL_LINES = 5;

    private final int location;
    private final int action;

    public ChartCanvasLineEvent(int location, int action) {
        this.location = location;
        this.action = action;
    }

    public int getLocation() {
        return location;
    }

    public int getAction() {
        return action;
    }
}
