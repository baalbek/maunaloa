package maunaloa.views.charts;

import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 15:58
 */
public class DraggableLine extends AbstractSelectableLine {
    //region Init
    private final Line line ;
    private final Circle startAnchor ;
    private final Circle endAnchor ;
    private final Group group ;
    private final DoubleProperty anchorRadius;
    private final BooleanProperty anchorsVisible ;

    public DraggableLine(Line line) {
        this.line = line;
        line.setStrokeWidth(STROKE_WIDTH_NORMAL);
        anchorRadius = new SimpleDoubleProperty(5);
        anchorsVisible = new SimpleBooleanProperty(true);
        startAnchor = createAnchor(line.startXProperty(), line.startYProperty());
        endAnchor = createAnchor(line.endXProperty(), line.endYProperty());
        group = new Group();
        group.getChildren().addAll(line, startAnchor, endAnchor);
        addMouseEvents(line);
    }

    public DraggableLine(double x1, double y1, double x2, double y2) {
        this(new Line(x1, y1, x2, y2));
    }
    //endregion Init

    //region Public Methods
    public Group view() {
        return group;
    }

    public void setAnchorsVisible(boolean visible) {
        anchorsVisible.set(visible);
    }

    @Override
    public Line getLine() {
        return line;
    }
    //endregion Public Methods

    //region Private Methods
    private Circle createAnchor(DoubleProperty x, DoubleProperty y) {
        final Circle anchor = new Circle();
        anchor.centerXProperty().bindBidirectional(x);
        anchor.centerYProperty().bindBidirectional(y);
        anchor.radiusProperty().bind(anchorRadius);
        anchor.visibleProperty().bind(anchorsVisible);
        anchor.setStrokeWidth(0.5);
        anchor.setFill(Color.TRANSPARENT);
        anchor.setStroke(Color.BLACK);
        //anchor.getStyleClass().add("draggable-line-anchor");

        final ObjectProperty<Point2D> mousePressPoint = new SimpleObjectProperty<>();
        anchor.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            mousePressPoint.set(new Point2D(event.getX(), event.getY()));
            //onMousePressed();
            event.consume();
        });
        anchor.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (mousePressPoint.get() != null) {
                setAnchorsVisible(false);
                double deltaX = event.getX()-mousePressPoint.get().getX();
                double deltaY = event.getY()-mousePressPoint.get().getY();
                mousePressPoint.set(new Point2D(event.getX(), event.getY()));
                double oldCenterX = anchor.getCenterX() ;
                double oldCenterY = anchor.getCenterY();
                anchor.setCenterX(oldCenterX+deltaX);
                anchor.setCenterY(oldCenterY + deltaY);
                //onMouseDragged(event);
                event.consume();
            }
        });
        anchor.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            setAnchorsVisible(true);
            mousePressPoint.set(null) ;
            if (onMouseReleased != null) {
                onMouseReleased.apply(event, anchor);
            }
            event.consume();
        });
        return anchor;
    }

    //endregion Private Methods
}
