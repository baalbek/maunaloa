package maunaloa.views;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import oahu.exceptions.NotImplementedException;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;

import java.util.ArrayList;
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

    private IBoundaryRuler vruler;
    private IRuler hruler;

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


        super(startX, startY, endX, endY, anchorRadius);

        this.hruler = hruler;

        this.vruler = (IBoundaryRuler)vruler;


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
    //endregion

    //region Private Methods
    private DoubleBinding createBinding(final double level, final boolean isFlipped) {
        return new DoubleBinding() {
            {
                super.bind(getLine().startYProperty(), getLine().endYProperty());
            }
            @Override
            protected double computeValue() {
                if (isFlipped == true) {
                    return getLine().getStartY() - ((getLine().getEndY() - getLine().getStartY()) * level);
                }
                else {
                    return getLine().getStartY() + ((getLine().getEndY() - getLine().getStartY()) * level);
                }
            }
        };
    }
    /*
    private DoubleBinding createBindingNegate(final double level) {
        return new DoubleBinding() {
            {
                super.bind(line.startYProperty(),line.endYProperty());
            }
            @Override
            protected double computeValue() {
                return line.getStartY() - ((line.getEndY() - line.getStartY()) * level);
            }
        };
    }
    //*/

    private Line createFibLine(DoubleBinding db, Color color) {
        Line newLine = new Line();
        newLine.setStroke(color);
        newLine.setEndX(vruler.getLowerRight().getX());
        newLine.startYProperty().bind(db);
        newLine.endYProperty().bind(db);
        newLine.startXProperty().bind(getLine().endXProperty());
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

    //region Properties

    //endregion
}
