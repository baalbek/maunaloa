package maunaloa.views;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.models.MaunaloaFacade;
import maunaloax.domain.MongoDBResult;
import maunaloax.models.ChartWindowDressingModel;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.joda.time.DateMidnight;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 20.11.13
 * Time: 09:40
 */
public class Level extends AbstractSelectable implements CanvasGroup, MongodbLine {
    //region Init
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private Group group ;
    private Circle anchor ;
    private Text valueLabel;
    private DoubleProperty anchorRadius = new SimpleDoubleProperty(7);
    private final IBoundaryRuler ruler;
    private final double levelValue;
    private double valueLabelDeltaX = 20.0;
    private double valueLabelDeltaY = 8.0;
    private final Color lineColor;
    private Line line;

    public Level(double levelValue, IRuler ruler) {
        this(levelValue, ruler, Color.BLACK);
    }

    public Level(double levelValue, IRuler ruler, Color lineColor) {
        this.ruler = (IBoundaryRuler)ruler;
        this.levelValue = levelValue;
        this.lineColor = lineColor;
    }

    public static Level createFromPix(double pix, IRuler ruler, Color lineColor) {
        return new Level(pix, ruler, lineColor);
    }


    //endregion Init

    //region Private Methods
    private void createLevel(double value, Color lineColor, String text){
        Point2D pt0 = ruler.getUpperLeft();
        Point2D pt = ruler.getLowerRight();
        double yBe = ruler.calcPix(value);
        line = new Line(pt0.getX() + 50,yBe,pt.getX(),yBe);
        line.setStroke(lineColor);

        valueLabel = new Text(pt0.getX()+ 50 + valueLabelDeltaX,yBe-valueLabelDeltaY,text);
        anchor = createAnchor(line, valueLabel);

        group.getChildren().addAll(line,anchor);
        group.getChildren().add(valueLabel);

        addEvents(line);

    }

    private Circle createAnchor(Line line, Text label) {
        final Circle anchor = new Circle();
        anchor.setCenterX(line.getStartX());
        anchor.setCenterY(line.getStartY());

        line.startXProperty().bindBidirectional(anchor.centerXProperty());
        line.startYProperty().bindBidirectional(anchor.centerYProperty());
        line.endYProperty().bindBidirectional(anchor.centerYProperty());

        anchor.radiusProperty().bind(anchorRadius);
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

                    double yVal = (Double) ruler.calcValue(anchor.getCenterY());

                    valueLabel.setText(String.format("%.1f", yVal));
                    valueLabel.setX(anchor.getCenterX()+valueLabelDeltaX);
                    valueLabel.setY(anchor.getCenterY()-valueLabelDeltaY);
                    // onMouseDragged(event);

                    event.consume();
                }
            }
        });
        anchor.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePressPoint.set(null) ;
                // onMouseReleased(event,anchor);
                event.consume();
            }
        });
        return anchor;
    }

    private BasicDBObject coord(Circle anchor) {
        DateMidnight dx = (DateMidnight)ruler.calcValue(anchor.getCenterX());
        double valY = (Double)ruler.calcValue(anchor.getCenterY());
        BasicDBObject curCoord = new BasicDBObject("x", dx.toDate());
        curCoord.append("y", valY);
        return curCoord;
    }
    //endregion Private Methods

    //region interface CanvasGroup
    @Override
    public Node view() {
        if (group == null) {
            group = new Group();
            createLevel(levelValue, lineColor, String.format("%.1f", levelValue));
        }
        return group;
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
    //endregion interface CanvasGroup

    //region interface MongodbLine
    @Override
    public ObjectId getMongodbId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMongodbId(ObjectId id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getActive() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setActive(boolean value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BasicDBObject coord(int pt) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addComment(String comment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getComments() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MongoDBResult save(ChartCanvasController controller) {
        DBObject p0 = coord(anchor);
        MongoDBResult result = null;
        MaunaloaFacade facade = controller.getModel();
        String ticker = controller.getTicker().getTicker();
        long curLoc = controller.getLocation();
        switch (getStatus()) {
            case CanvasGroup.SELECTED:
                result = facade.getWindowDressingModel().save(
                        ChartWindowDressingModel.MONGO_LEVELS,
                        ticker,
                        curLoc,
                        p0,
                        null);
                break;
            case CanvasGroup.SAVED_TO_DB_SELECTED:
                result = new MongoDBResult(facade.getWindowDressingModel().updateCoord(
                        ChartWindowDressingModel.MONGO_LEVELS,
                        getMongodbId(),
                        p0,
                        null));
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
    //endregion interface MongodbLine
}
