package maunaloa.views.charts;

import javafx.beans.property.*;
import javafx.event.EventHandler;
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
public class DraggableLine {
    //region Init
    private Line line ;
    private Circle startAnchor ;
    private Circle endAnchor ;
    private Group group ;
    private DoubleProperty anchorRadius;
    private BooleanProperty anchorsVisible ;

    private static double STROKE_WIDTH_NORMAL = 1.0;
    private static double STROKE_WIDTH_SELECTED = 4.0;

    public DraggableLine(double startX, double startY, double endX, double endY) {
        anchorRadius = new SimpleDoubleProperty(5);
        line = new Line(startX, startY, endX, endY);
        line.setStrokeWidth(STROKE_WIDTH_NORMAL);
        anchorsVisible = new SimpleBooleanProperty(true);
        startAnchor = createAnchor(line.startXProperty(), line.startYProperty());
        endAnchor = createAnchor(line.endXProperty(), line.endYProperty());
        group = new Group();
        group.getChildren().addAll(line, startAnchor, endAnchor);
    }
    //endregion Init

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

        /*
        final ObjectProperty<Point2D> mousePressPoint = new SimpleObjectProperty<>();
        anchor.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePressPoint.set(new Point2D(event.getX(), event.getY()));
                //onMousePressed();
                event.consume();
            }
        });
        anchor.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (mousePressPoint.get() != null) {
                    double deltaX = event.getX()-mousePressPoint.get().getX();
                    double deltaY = event.getY()-mousePressPoint.get().getY();
                    mousePressPoint.set(new Point2D(event.getX(), event.getY()));
                    double oldCenterX = anchor.getCenterX() ;
                    double oldCenterY = anchor.getCenterY();
                    anchor.setCenterX(oldCenterX+deltaX);
                    anchor.setCenterY(oldCenterY+deltaY);
                    onMouseDragged(event);
                    event.consume();
                }
            }
        });
        anchor.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePressPoint.set(null) ;
                onMouseReleased(event,anchor);
                event.consume();
            }
        });
        //*/
        return anchor;
    }
    //endregion Private Methods

}
