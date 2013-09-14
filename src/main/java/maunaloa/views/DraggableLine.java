package maunaloa.views;

import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 3/31/13
 * Time: 7:42 PM
 */
public abstract class DraggableLine implements CanvasGroup {
    protected Line line ;
    protected Circle startAnchor ;
    protected Circle endAnchor ;
    protected Group group ;

    private DoubleProperty anchorRadius ;
    private BooleanProperty anchorsVisible ;

    public DraggableLine(double startX, double startY, double endX, double endY, double anchorRadius) {
        line = new Line(startX, startY, endX, endY);
        this.anchorRadius = new SimpleDoubleProperty(anchorRadius);
        anchorsVisible = new SimpleBooleanProperty(true);
        startAnchor = createAnchor(line.startXProperty(), line.startYProperty());
        endAnchor = createAnchor(line.endXProperty(), line.endYProperty());
        group = new Group();
        group.getChildren().addAll(line, startAnchor, endAnchor);
    }


    //region Abstract Methods
    //public abstract void onMousePressed();
    protected abstract void onMouseReleased(MouseEvent event);
    protected abstract void onMouseDragged(MouseEvent event);
    //endregion

    //region Interface Methods
    @Override
    public Node view() {
        return group ;
    }

    //endregion

    //region Private Methods
    private Circle createAnchor(DoubleProperty x, DoubleProperty y) {
        final Circle anchor = new Circle();
        anchor.centerXProperty().bindBidirectional(x);
        anchor.centerYProperty().bindBidirectional(y);
        anchor.radiusProperty().bind(anchorRadius);
        anchor.visibleProperty().bind(anchorsVisible);
        anchor.getStyleClass().add("draggable-line-anchor");

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
                onMouseReleased(event);
                event.consume();
            }
        });
        return anchor;
    }
    //endregion


    //region Properties
    public DoubleProperty anchorRadiusProperty() {
        return anchorRadius ;
    }

    public double getAnchorRadius() {
        return anchorRadius.get();
    }

    public void setAnchorRadius(double radius) {
        anchorRadius.set(radius);
    }

    public BooleanProperty anchorsVisibleProperty() {
        return anchorsVisible ;
    }

    public boolean isAnchorsVisible() {
        return anchorsVisible.get();
    }

    public void setAnchorsVisible(boolean visible) {
        anchorsVisible.set(visible);
    }

    public double getStartX() {
        return line.getStartX();
    }

    public void setStartX(double x) {
        line.setStartX(x);
    }

    public double getStartY() {
        return line.getStartY();
    }

    public void setStartY(double y) {
        line.setStartY(y);
    }

    public double getEndX() {
        return line.getEndX();
    }

    public void setEndX(double x) {
        line.setEndX(x);
    }

    public double getEndY() {
        return line.getEndY();
    }

    public void setEndY(double y) {
        line.setEndY(y);
    }
    //endregion
}