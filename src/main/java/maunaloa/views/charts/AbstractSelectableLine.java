package maunaloa.views.charts;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import maunaloa.StatusCodes;
import oahu.functional.Procedure2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    protected static Map<Integer,Color> statusColors;
    static {
        statusColors = new HashMap<>();
        /*statusColors.put(StatusCodes.UNSELECTED, Color.BLACK);
        statusColors.put(StatusCodes.SELECTED, Color.RED);*/
        statusColors.put(StatusCodes.ENTITY_CLEAN, Color.BLACK);
        statusColors.put(StatusCodes.ENTITY_DIRTY, Color.YELLOWGREEN);
        statusColors.put(StatusCodes.ENTITY_IS_INACTIVE, Color.BLUEVIOLET);
        statusColors.put(StatusCodes.ENTITY_TO_BE_INACTIVE, Color.DARKCYAN);
        statusColors.put(StatusCodes.ENTITY_NEW, Color.ORANGERED);
    }


    public void addMouseEvents(final Line line) {
        line.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            line.setStrokeWidth(STROKE_WIDTH_ENTERED);
        });
        line.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            //double sw = getStatus() == StatusCodes.UNSELECTED ? STROKE_WIDTH_NORMAL : STROKE_WIDTH_SELECTED;
            line.setStrokeWidth(STROKE_WIDTH_NORMAL);
        });
        line.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.isShiftDown()) {
                if (onMouseReleasedShift != null) {
                    onMouseReleasedShift.accept(e);
                }
            }
            else {
                switch (status.get()) { //(getStatus()) {
                    case StatusCodes.UNSELECTED:
                        setStatus(StatusCodes.SELECTED);
                        break;
                    case StatusCodes.SELECTED:
                        setStatus(StatusCodes.UNSELECTED);
                        break;
                }
            }
        });
    }

    private IntegerProperty status = new SimpleIntegerProperty(StatusCodes.UNSELECTED);

    public void setStatus(int value) {
        status.set(value);
        Line line = getLine();
        //line.setStroke(statusColors.get(status.get()));
        if (value == StatusCodes.SELECTED) {
            line.getStrokeDashArray().addAll(10d,10d);
        }
        else {
            line.getStrokeDashArray().clear();
        }
    }
    /*
    public int getStatus() {
        return status.get();
    }
    //*/

    public IntegerProperty statusProperty() {
        return status;
    }

    //region Events
    private Consumer<MouseEvent> onMouseReleasedShift;

    public void setOnMouseReleasedShift(Consumer<MouseEvent> onMouseReleasedShift) {
        this.onMouseReleasedShift = onMouseReleasedShift;
    }
    protected Procedure2<MouseEvent,Circle> onMouseReleased;
    public void setOnMouseReleased(Procedure2<MouseEvent,Circle> onMouseReleased) {
        this.onMouseReleased = onMouseReleased;
    }
    //endregion Events

    public abstract Line getLine();

}
