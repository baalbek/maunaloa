package maunaloa.views;

import com.mongodb.BasicDBObject;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 3/31/13
 * Time: 7:42 PM
 */
public abstract class DraggableLine implements CanvasGroup, MongodbLine {
    //region Init
    private Line line ;
    private ObjectId mongodbId;
    private boolean active;
    private long location;
    protected Circle startAnchor ;
    protected Circle endAnchor ;
    protected Group group ;
    protected IBoundaryRuler vruler;
    protected IRuler hruler;

    private DoubleProperty anchorRadius ;
    private BooleanProperty anchorsVisible ;

    private static double STROKE_WIDTH_NORMAL = 1.0;
    private static double STROKE_WIDTH_SELECTED = 4.0;

    private int status = CanvasGroup.NORMAL;
    //endregion Init

    //region Constructors
    public DraggableLine(ObjectId mongodbId,
                         boolean active,
                         long location,
                         BasicDBObject p1,
                         BasicDBObject p2,
                         IRuler hruler,
                         IRuler vruler) {
        this.mongodbId = mongodbId;
        this.location = location;

        this.line = null;
    }
    public DraggableLine(double startX,
                         double startY,
                         double endX,
                         double endY,
                         double anchorRadius,
                         IRuler hruler,
                         IRuler vruler) {
        this(startX,startY,endX,endY,anchorRadius);
        this.hruler = hruler;
        this.vruler = (IBoundaryRuler)vruler;
    }
    public DraggableLine(double startX, double startY, double endX, double endY, double anchorRadius) {
        init(startX, startY, endX, endY, anchorRadius);
    }

    private void init(double startX, double startY, double endX, double endY, double anchorRadius) {
        line = new Line(startX, startY, endX, endY);
        line.setStrokeWidth(STROKE_WIDTH_NORMAL);
        addEvents(line);
        this.anchorRadius = new SimpleDoubleProperty(anchorRadius);
        anchorsVisible = new SimpleBooleanProperty(true);
        startAnchor = createAnchor(line.startXProperty(), line.startYProperty());
        endAnchor = createAnchor(line.endXProperty(), line.endYProperty());
        group = new Group();
        group.getChildren().addAll(line, startAnchor, endAnchor);
    }
    //endregion Constructors

    //region Abstract Methods
    //public abstract void onMousePressed();
    protected abstract void onMouseReleased(MouseEvent event, Circle anchor);
    protected abstract void onMouseDragged(MouseEvent event);
    //endregion

    //region Interface CanvasGroup
    @Override
    public Node view() {
        return group ;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }
    //endregion Interface CanvasGroup

    //region interface MongodbLine
    @Override
    public ObjectId getMongodbId() {
        return mongodbId;
    }
    @Override
    public boolean getActive() {
        return active;
    }

    @Override
    public void setActive(boolean value) {
        active = value;
    }


    @Override
    public BasicDBObject coord(int pt) {
        Circle anchor = pt == MongodbLine.P1 ? startAnchor : endAnchor;
        return coord(anchor);
    }
    @Override
    public long getLocation() {
        return location;
    }
    public void setLocation(long value) {
        location = value;
    }
    //endregion Interface MongodbLine

    //region Events
    private void addEvents(final Line line) {
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
                        if (getStatus() == CanvasGroup.NORMAL) {
                            setStatus(CanvasGroup.SELECTED);
                            line.setStroke(Color.RED);
                        }
                        else if (getStatus() == CanvasGroup.SELECTED) {
                            setStatus(CanvasGroup.NORMAL);
                            line.setStroke(Color.BLACK);
                        }
                    }
                });
    }


    //endregion Events

    //region Private Methods
    private BasicDBObject coord(Circle anchor) {
        Date dx = (Date)hruler.calcValue(anchor.getCenterX());
        double valY = (Double)vruler.calcValue(anchor.getCenterY());
        BasicDBObject curCoord = new BasicDBObject("x", dx);
        curCoord.append("y", valY);
        return curCoord;
    }

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

    public Line getLine() {
        return line;
    }
    //endregion
}