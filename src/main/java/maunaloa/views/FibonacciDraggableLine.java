package maunaloa.views;

import com.mongodb.BasicDBObject;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import oahu.exceptions.NotImplementedException;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 3/31/13
 * Time: 7:43 PM
 */

public class FibonacciDraggableLine extends DraggableLine {

    //region Init


    static List<Color> _colors;
    static int colorsIndex = 0;

    static double PHI = 0.618034;
    static double PHI_EXT = 1.272;


    static int getColorsIndex() {
        if (colorsIndex >= _colors.size()) {
            colorsIndex = 0;
        }
        return colorsIndex++;
    }
    static Color getCurrentColor() {
        if (_colors == null) {
            _colors = new ArrayList<>();
            _colors.add(Color.BLUEVIOLET);
            _colors.add(Color.BROWN);
            _colors.add(Color.CHOCOLATE);
            _colors.add(Color.GREEN);
        }
        return _colors.get(getColorsIndex());
    }

    //endregion Init

    //region Constructors
    /*
    public FibonacciDraggableLine(ObjectId mongodbId,
                                  boolean active,
                                  long location,
                                  BasicDBObject p1,
                                  BasicDBObject p2,
                                  IRuler hruler,
                                  IRuler vruler) {
        this.mongodbId = mongodbId;
        this.active = active;
        this.location = location;


        this.hruler = hruler;
        this.vruler = (IBoundaryRuler)vruler;

        double startX = hruler.calcPix((Date)p1.get("x"));
        double startY = vruler.calcPix((Double)p1.get("y"));
        double endX = hruler.calcPix((Date)p2.get("x"));
        double endY = vruler.calcPix((Double)p2.get("y"));
    }
    */


    public FibonacciDraggableLine(Line line, IRuler hruler, IRuler vruler, boolean fib1272Extensions) {
        this(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY(), 7, hruler, vruler, fib1272Extensions);
    }


    public FibonacciDraggableLine(double startX,
                                  double startY,
                                  double endX,
                                  double endY,
                                  double anchorRadius,
                                  IRuler hruler,
                                  IRuler vruler,
                                  boolean fib1272Extensions) {


        super(startX, startY, endX, endY, anchorRadius,hruler,vruler);

        init(startX, startY, endX, endY, fib1272Extensions);
    }

    private void init(double startX, double startY, double endX, double endY, boolean fib1272Extensions) {
        double x = Math.max(startX, endX);

        double y = Math.min(startY, endY);

        Color curColor = getCurrentColor();
        group.getChildren().add(createFibLine(createBinding(0.5,false), curColor));

        group.getChildren().add(createFibLine(createBinding(PHI,false), curColor));

        group.getChildren().add(createFibLine(createBinding(PHI*PHI,false),curColor));

        if (fib1272Extensions == true) {
            group.getChildren().add(createFibLine(createBinding(PHI_EXT,false),curColor));
            group.getChildren().add(createFibLine(createBinding(PHI_EXT-1.0,true),curColor));
        }
    }
    //endregion Constructors

    //region Private Methods
    private DoubleBinding createBinding(final double level, final boolean isFlipped) {
        return new DoubleBinding() {
            {
                super.bind(getLine().startYProperty(), getLine().endYProperty());
            }
            @Override
            protected double computeValue() {
                double adjustment = (getLine().getEndY() - getLine().getStartY()) * level;
                double y = getLine().getStartY();
                return (isFlipped == true) ? y - adjustment : y + adjustment;
            }
        };
    }
    private Line createFibLine(DoubleBinding db, Color color) {
        Line newLine = new Line();
        newLine.setStroke(color);
        newLine.setEndX(vruler.getLowerRight().getX());
        newLine.startYProperty().bind(db);
        newLine.endYProperty().bind(db);
        Line line = getLine();

        DoubleProperty dp = line.getStartX() < line.getEndX() ? line.startXProperty() : line.endXProperty();

        newLine.startXProperty().bind(dp);
        return newLine;
    }
    //endregion

    //region Implemented Abstract Methods
    @Override
    protected void onMouseReleased(MouseEvent event, Circle anchor) {
        anchor.setCenterX(hruler.snapTo(anchor.getCenterX()));
        setAnchorsVisible(true);
    }

    @Override
    protected void onMouseDragged(MouseEvent event) {
        setAnchorsVisible(false);
    }


    //endregion

    //region interface MongodbLine
    //endregion MongodbLine

    //region Properties

    //endregion
}
