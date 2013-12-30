package maunaloa.views;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 11/28/13
 * Time: 11:35 PM
 */
public abstract class AbstractSelectable {
    public static double STROKE_WIDTH_NORMAL = 1.0;
    public static double STROKE_WIDTH_SELECTED = 4.0;

    protected int status = CanvasGroup.NORMAL;

    protected static Map<Integer,Color> statusColors;
    static {
        statusColors = new HashMap<>();
        statusColors.put(CanvasGroup.NORMAL, Color.BLACK);
        statusColors.put(CanvasGroup.SELECTED, Color.RED);
        statusColors.put(CanvasGroup.SAVED_TO_DB, Color.GREEN);
        statusColors.put(CanvasGroup.SAVED_TO_DB_SELECTED, Color.AQUAMARINE);
    }


    public void addEvents(final Line line) {
        line.addEventFilter(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        line.setStrokeWidth(STROKE_WIDTH_SELECTED);
                    }
                });
        line.addEventFilter(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        line.setStrokeWidth(STROKE_WIDTH_NORMAL);
                    }
                });
        line.addEventFilter(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        switch (getStatus()) {
                            case CanvasGroup.NORMAL:
                                setStatus(CanvasGroup.SELECTED);
                                break;
                            case CanvasGroup.SELECTED:
                                setStatus(CanvasGroup.NORMAL);
                                break;
                            case CanvasGroup.SAVED_TO_DB:
                                setStatus(CanvasGroup.SAVED_TO_DB_SELECTED);
                                break;
                            case CanvasGroup.SAVED_TO_DB_SELECTED:
                                setStatus(CanvasGroup.SAVED_TO_DB);
                                break;
                        }
                    }
                });
    }

    public abstract void setStatus(int status);
    public abstract int getStatus();
}
