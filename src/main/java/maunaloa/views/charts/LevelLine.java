package maunaloa.views.charts;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import maunaloa.StatusCodes;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;

import java.util.function.Function;

/**
 * Created by rcs on 5/3/14.
 *
 */
public class LevelLine extends AbstractSelectableLine {

    //region Init
    private IBoundaryRuler ruler;
    private double levelValue;
    private double valueLabelDeltaX = 20.0;
    private double valueLabelDeltaY = 8.0;
    //private DoubleProperty anchorRadius = new SimpleDoubleProperty(7);
    private Function<Double,String> valueLabelTextFn;
    private Group group ;
    private Line line;
    private Circle anchor;
    private Text valueLabel;

    public LevelLine(double levelValue, IRuler ruler) {
        //this(levelValue, ruler, Color.BLACK);
        this.ruler = (IBoundaryRuler)ruler;
        this.levelValue = levelValue;
        //jthis.lineColor = statusColors.get(StatusCodes.ENTITY_NEW);
    }

    //endregion Init

    //region Properties
    public void setValueLabelFunc(Function<Double,String> fun) {
        valueLabelTextFn = fun;
    }
    //endregion Properties


    //region Private Methods
    private void createLevel(){
        Point2D pt0 = ruler.getUpperLeft();
        Point2D pt = ruler.getLowerRight();
        double yBe = ruler.calcPix(levelValue);
        line = new Line(pt0.getX() + 50,yBe,pt.getX(),yBe);
        line.setStroke(statusColors.get(StatusCodes.ENTITY_NEW));

        valueLabel = new Text(pt0.getX()+ 50 + valueLabelDeltaX,yBe-valueLabelDeltaY,valueLabelText());
        anchor = createAnchor();

        group = new Group();
        group.getChildren().addAll(line,anchor);
        group.getChildren().add(valueLabel);

        addMouseEvents(line);
    }


    private String valueLabelText () {
        if (valueLabelTextFn == null) {
            return String.format("%.1f", levelValue);
        }
        else {
            return String.format("%s", valueLabelTextFn.apply(levelValue));
        }
    }

    private Circle createAnchor() {
        final Circle anchor = new Circle();
        anchor.setCenterX(line.getStartX());
        anchor.setCenterY(line.getStartY());

        line.startXProperty().bindBidirectional(anchor.centerXProperty());
        line.startYProperty().bindBidirectional(anchor.centerYProperty());
        line.endYProperty().bindBidirectional(anchor.centerYProperty());

        anchor.setRadius(7); //anchor.radiusProperty().bind(anchorRadius);
        anchor.setStrokeWidth(0.5);
        anchor.setFill(Color.TRANSPARENT);
        anchor.setStroke(Color.BLACK);

        final ObjectProperty<Point2D> mousePressPoint = new SimpleObjectProperty<>();

        anchor.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            mousePressPoint.set(new Point2D(event.getX(), event.getY()));
            event.consume();
        });
        anchor.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (mousePressPoint.get() != null) {
                double deltaX = event.getX()-mousePressPoint.get().getX();
                double deltaY = event.getY()-mousePressPoint.get().getY();
                mousePressPoint.set(new Point2D(event.getX(), event.getY()));
                double oldCenterX = anchor.getCenterX() ;
                double oldCenterY = anchor.getCenterY();
                anchor.setCenterX(oldCenterX+deltaX);
                anchor.setCenterY(oldCenterY+deltaY);

                levelValue = (Double) ruler.calcValue(anchor.getCenterY());

                valueLabel.setX(anchor.getCenterX()+valueLabelDeltaX);
                valueLabel.setY(anchor.getCenterY()-valueLabelDeltaY);
                valueLabel.setText(valueLabelText());

                event.consume();
            }
        });
        anchor.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            mousePressPoint.set(null) ;
            if (onMouseReleased != null) {
                onMouseReleased.apply(event, anchor);
            }
            event.consume();
        });
        return anchor;
    }

    //endregion Private Methods

    //region Public |Methods
    public void updateRuler(IRuler ruler) {
        this.ruler = (IBoundaryRuler)ruler;
        double newY = ruler.calcPix(levelValue);
        anchor.setCenterY(newY);
        valueLabel.setY(newY-valueLabelDeltaY);
    }
    public Group view() {
        if (group == null) {
            createLevel();
        }
        return group;
    }

    public double getLevelValue() {
        return levelValue;
    }
    public void updateColorFor(int statusCode) {
        line.setStroke(statusColors.get(statusCode));
    }
    //endregion Public Methods

    //region AbstractSelectableLine
    @Override
    public Line getLine() {
        return line;
    }

    //endregion AbstractSelectableLine
}
