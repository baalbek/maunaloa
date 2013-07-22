package maunaloa.views;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 3/31/13
 * Time: 7:43 PM
 */

public class FibonacciDraggableLine extends DraggableLine {

    //region Init



    static double PHI = 0.618034;

    private IBoundaryRuler vruler;


    public FibonacciDraggableLine(Line line, IRuler vruler) {
        this(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY(), 7, vruler);
    }

    public FibonacciDraggableLine(double startX,
                                  double startY,
                                  double endX,
                                  double endY,
                                  double anchorRadius,
                                  IRuler vruler) {


        super(startX, startY, endX, endY, anchorRadius);

        this.vruler = (IBoundaryRuler)vruler;

        double x = Math.max(startX, endX);

        double y = Math.min(startY, endY);

        DoubleBinding y50 = new DoubleBinding() {
            {
                super.bind(line.startYProperty(),line.endYProperty());
            }
            @Override
            protected double computeValue() {
                return (line.startYProperty().get() + line.endYProperty().get()) * 0.5;
            }
        };

        DoubleBinding y618 = new DoubleBinding() {
            {
                super.bind(line.startYProperty(),line.endYProperty());
            }
            @Override
            protected double computeValue() {
                return line.getStartY() + (line.getEndY() - line.getStartY()) * PHI;
            }
        };

        DoubleBinding y382 = new DoubleBinding() {
            {
                super.bind(line.startYProperty(),line.endYProperty());
            }
            @Override
            protected double computeValue() {
                return line.getStartY() + (line.getEndY() - line.getStartY()) * PHI * PHI;
            }
        };

        group.getChildren().add(createFibLine(y50, Color.BLACK));

        group.getChildren().add(createFibLine(y618,Color.BLACK));

        group.getChildren().add(createFibLine(y382,Color.BLACK));
    }
    //endregion

    //region Private Methods
    private Line createFibLine(DoubleBinding db, Color color) {
        Line newLine = new Line();
        newLine.setStroke(color);
        newLine.setEndX(vruler.getLowerRight().getX());
        newLine.startYProperty().bind(db);
        newLine.endYProperty().bind(db);
        newLine.startXProperty().bind(line.endXProperty());
        return newLine;
    }
    //endregion

    //region Implemented Abstract Methods


    @Override
    protected void onMouseReleased(MouseEvent event) {
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
