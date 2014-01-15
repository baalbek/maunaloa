package maunaloa.views;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.models.MaunaloaFacade;
import maunaloax.domain.MongoDBResult;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import org.bson.types.ObjectId;
import org.joda.time.DateMidnight;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 3/31/13
 * Time: 7:42 PM
 */
public abstract class DraggableLine extends AbstractSelectable implements CanvasGroup, MongodbLine {
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



    private List<String> comments;


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
        line.setStroke(statusColors.get(status));
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
    public void setMongodbId(ObjectId id) {
        mongodbId = id;
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
        Circle anchor;
        if (pt == MongodbLine.P1) {
            anchor = startAnchor.getCenterX() < endAnchor.getCenterX() ? startAnchor : endAnchor;
        }
        else {
            anchor = endAnchor.getCenterX() > startAnchor.getCenterX() ? endAnchor : startAnchor;
        }
        return coord(anchor);
    }

    @Override
    public long getLocation() {
        return location;
    }
    public void setLocation(long value) {
        location = value;
    }
    @Override
    public void addComment(String comment) {
        getComments().add(comment);
    }
    @Override
    public List<String> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }
    @Override
    public MongoDBResult save(ChartCanvasController controller) {
        DBObject p1 = coord(MongodbLine.P1);
        DBObject p2 = coord(MongodbLine.P2);
        MongoDBResult result = null;
        MaunaloaFacade facade = controller.getModel();
        String ticker = controller.getTicker().getTicker();
        long curLoc = controller.getLocation();
        switch (getStatus()) {
            case CanvasGroup.SELECTED:
                result = facade.getWindowDressingModel().saveFibonacci(ticker,curLoc,p1,p2);
                break;
            case CanvasGroup.SAVED_TO_DB_SELECTED:
                result = new MongoDBResult(facade.getWindowDressingModel().updateCoord(getMongodbId(),p1,p2));
                break;
            default:
                result = null;
                break;
        }
        if ((result != null) && (result.isOk() == true)) {
            setStatus(CanvasGroup.SAVED_TO_DB);
        }
        return result;
    }
    //endregion Interface MongodbLine

    //region Private Methods
    private BasicDBObject coord(Circle anchor) {
        DateMidnight dx = (DateMidnight)hruler.calcValue(anchor.getCenterX());
        double valY = (Double)vruler.calcValue(anchor.getCenterY());
        BasicDBObject curCoord = new BasicDBObject("x", dx.toDate());
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
