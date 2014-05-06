package maunaloa.views.charts;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;

/**
 * Created by rcs on 5/3/14.
 */
public class LevelLine extends AbstractSelectableLine {

    //region Init
    private final IBoundaryRuler ruler;
    private double levelValue;
    private final Color lineColor;
    private double valueLabelDeltaX = 20.0;
    private double valueLabelDeltaY = 8.0;
    private DoubleProperty anchorRadius = new SimpleDoubleProperty(7);
    private Group group ;
    private Line line;

    public LevelLine(double levelValue, IRuler ruler) {
        this(levelValue, ruler, Color.BLACK);
    }

    public LevelLine(double levelValue, IRuler ruler, Color lineColor) {
        this.ruler = (IBoundaryRuler)ruler;
        this.levelValue = levelValue;
        this.lineColor = lineColor;
    }
    //endregion Init

    //region Private Methods
    private void createLevel(){
        Point2D pt0 = ruler.getUpperLeft();
        Point2D pt = ruler.getLowerRight();
        double yBe = ruler.calcPix(levelValue);
        line = new Line(pt0.getX() + 50,yBe,pt.getX(),yBe);
        line.setStroke(lineColor);

        //double yVal = (Double) ruler.calcValue(anchor.getCenterY());

        String labelText = String.format("%.1f", levelValue);
        Text valueLabel = new Text(pt0.getX()+ 50 + valueLabelDeltaX,yBe-valueLabelDeltaY,labelText);
        Circle anchor = createAnchor(line, valueLabel);

        group = new Group();
        group.getChildren().addAll(line,anchor);
        group.getChildren().add(valueLabel);

        addMouseEvents(line);
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
        anchor.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            mousePressPoint.set(new Point2D(event.getX(), event.getY()));
            //onMousePressed();
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
                //double yVal = (Double) ruler.calcValue(anchor.getCenterY());

                label.setText(String.format("%.1f", levelValue));
                label.setX(anchor.getCenterX()+valueLabelDeltaX);
                label.setY(anchor.getCenterY()-valueLabelDeltaY);
                // onMouseDragged(event);

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

    //region Private Methods
    public Group view() {
        if (group == null) {
            createLevel();
        }
        return group;
    }

    public double getLevelValue() {
        return levelValue;
    }
    //endregion Private Methods

    //region AbstractSelectableLine
    @Override
    public Line getLine() {
        return line;
    }
    //endregion AbstractSelectableLine
}
