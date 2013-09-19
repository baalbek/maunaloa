package maunaloa.views;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
    static double PHI_EXT = 1.27;

    private boolean hasExtensions = false;

    private IBoundaryRuler vruler;

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

    public FibonacciDraggableLine(Line line, IRuler vruler) {
        this(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY(), 7, vruler);
    }

    public FibonacciDraggableLine(Line line, IRuler vruler, boolean hasExtensions) {
        this(line,vruler);
        this.hasExtensions = hasExtensions;
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

        Color curColor = getCurrentColor();
        group.getChildren().add(createFibLine(createBinding(0.5), curColor));

        group.getChildren().add(createFibLine(createBinding(PHI), curColor));

        group.getChildren().add(createFibLine(createBinding(PHI*PHI),curColor));

        if (hasExtensions == true) {
            group.getChildren().add(createFibLine(createBinding(PHI_EXT),curColor));
        }
    }
    //endregion

    //region Private Methods
    private DoubleBinding createBinding(final double level) {
        return new DoubleBinding() {
            {
                super.bind(line.startYProperty(),line.endYProperty());
            }
            @Override
            protected double computeValue() {
                return line.getStartY() + (line.getEndY() - line.getStartY()) * level;
            }
        };
    }

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
