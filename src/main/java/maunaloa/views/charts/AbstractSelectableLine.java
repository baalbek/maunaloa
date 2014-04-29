package maunaloa.views.charts;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 29.04.14
 * Time: 15:49
 */
public abstract class AbstractSelectableLine {

    public static double STROKE_WIDTH_NORMAL = 1.0;
    public static double STROKE_WIDTH_SELECTED = 2.0;
    public static double STROKE_WIDTH_ENTERED = 4.0;

    //region Status Codes
    //endregion Status Codes


    protected static Map<Integer,Color> statusColors;
    static {
        statusColors = new HashMap<>();
        statusColors.put(ChartStatusCodes.NORMAL, Color.BLACK);
        statusColors.put(ChartStatusCodes.SELECTED, Color.RED);
        statusColors.put(ChartStatusCodes.SAVED_TO_DB, Color.GREEN);
        statusColors.put(ChartStatusCodes.SAVED_TO_DB_SELECTED, Color.AQUAMARINE);
    }


    public void addMouseEvents(final Line line) {
        line.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            line.setStrokeWidth(STROKE_WIDTH_ENTERED);
        });
        line.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            double sw = getStatus() == ChartStatusCodes.NORMAL ? STROKE_WIDTH_NORMAL : STROKE_WIDTH_SELECTED;
            line.setStrokeWidth(sw);
        });
        line.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            switch (getStatus()) {
                case ChartStatusCodes.NORMAL:
                    setStatus(ChartStatusCodes.SELECTED);
                    break;
                case ChartStatusCodes.SELECTED:
                    setStatus(ChartStatusCodes.NORMAL);
                    break;
                case ChartStatusCodes.SAVED_TO_DB:
                    setStatus(ChartStatusCodes.SAVED_TO_DB_SELECTED);
                    break;
                case ChartStatusCodes.SAVED_TO_DB_SELECTED:
                    setStatus(ChartStatusCodes.SAVED_TO_DB);
                    break;
            }
        });
    }

    private int status = ChartStatusCodes.NORMAL;
    public void setStatus(int status) {
        this.status = status;
        Line line = getLine();
        line.setStroke(statusColors.get(status));
        //line.setStrokeWidth(STROKE_WIDTH_SELECTED);
    }
    public int getStatus() {
        return status;
    }

    public abstract Line getLine();
}
