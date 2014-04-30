package maunaloa.views.charts;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;

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

    //region MaunaloaStatus Codes
    //endregion MaunaloaStatus Codes


    protected static Map<Integer,Color> statusColors;
    static {
        statusColors = new HashMap<>();
        statusColors.put(StatusCodes.UNSELECTED, Color.BLACK);
        statusColors.put(StatusCodes.SELECTED, Color.RED);
    }


    public void addMouseEvents(final Line line) {
        line.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            line.setStrokeWidth(STROKE_WIDTH_ENTERED);
        });
        line.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            double sw = getStatus() == StatusCodes.UNSELECTED ? STROKE_WIDTH_NORMAL : STROKE_WIDTH_SELECTED;
            line.setStrokeWidth(sw);
        });
        line.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            switch (getStatus()) {
                case StatusCodes.UNSELECTED:
                    setStatus(StatusCodes.SELECTED);
                    break;
                case StatusCodes.SELECTED:
                    setStatus(StatusCodes.UNSELECTED);
                    break;
            }
        });
    }

    //private int status = StatusCodes.UNSELECTED;
    private IntegerProperty status = new SimpleIntegerProperty(StatusCodes.UNSELECTED);

    public void setStatus(int value) {
        status.set(value);
        Line line = getLine();
        line.setStroke(statusColors.get(status.get()));
        //line.setStrokeWidth(STROKE_WIDTH_SELECTED);
    }
    public int getStatus() {
        return status.get();
    }

    public IntegerProperty  getStatusProperty() {
        return status;
    }

    public abstract Line getLine();
}
