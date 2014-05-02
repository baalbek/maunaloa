package maunaloa.entities.windowdressing;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.DraggableLine;
import maunaloa.views.charts.FinancialCoord;
import oahu.domain.Tuple;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 13:34
 */
public class FibLine implements ChartItem {
    //region Properties
    //private final MaunaloaChartViewModel viewModel;
    private final Tuple<IRuler> rulers;
    private ObjectId oid;
    private final String ticker;
    private final int location;
    private FinancialCoord fp1, fp2;
    private Tuple<Double> p1,p2;

    private DraggableLine dragLine;

    public ObjectId getOid() {
        return oid;
    }
    public void setOid(ObjectId oid) {
        this.oid = oid;
    }

    public String getTicker() {
        return ticker;
    }

    public int getLocation() {
        return location;
    }
    //public FinancialCoord financialCoord()
    //endregion Properties

    //region Private Methods
    private void setupDragLine() {
        dragLine.setOnMouseReleased((evt,anchor) -> {
            anchor.setCenterX(rulers.first().snapTo(anchor.getCenterX()));
        });
        Color curColor = getCurrentColor();
        dragLine.view().getChildren().add(createFibLine(createBinding(0.5,false), curColor));
        dragLine.view().getChildren().add(createFibLine(createBinding(PHI,false), curColor));

        dragLine.view().getChildren().add(createFibLine(createBinding(PHI*PHI,false),curColor));


        Line line = dragLine.getLine();
        if (line.getStartX() < line.getEndX()) {
            dragLine.view().getChildren().add(createFibLine(createBinding(PHI_EXT,false),curColor));
        }
        else {
            dragLine.view().getChildren().add(createFibLine(createBinding(PHI_EXT-1.0,true),curColor));
        }
    }
    static List<Color> _colors;
    static int colorsIndex = 0;
    static double PHI = 0.618034;
    static double PHI_EXT = 1.272;
    private static int getColorsIndex() {
        if (colorsIndex >= _colors.size()) {
            colorsIndex = 0;
        }
        return colorsIndex++;
    }
    private static Color getCurrentColor() {
        if (_colors == null) {
            _colors = new ArrayList<>();
            _colors.add(Color.BLUEVIOLET);
            _colors.add(Color.BROWN);
            _colors.add(Color.CHOCOLATE);
            _colors.add(Color.GREEN);
        }
        return _colors.get(getColorsIndex());
    }
    private DoubleBinding createBinding(final double level, final boolean isFlipped) {
        Line line = dragLine.getLine();
        return new DoubleBinding() {
            {
                super.bind(line.startYProperty(), line.endYProperty());
            }
            @Override
            protected double computeValue() {
                double adjustment = (line.getEndY() - line.getStartY()) * level;
                double y = line.getStartY();
                return (isFlipped == true) ? y - adjustment : y + adjustment;
            }
        };
    }
    private Line createFibLine(DoubleBinding db, Color color) {
        Line newLine = new Line();
        newLine.setStroke(color);
        newLine.setEndX(getVruler().getLowerRight().getX());
        newLine.startYProperty().bind(db);
        newLine.endYProperty().bind(db);
        Line line = dragLine.getLine();
        DoubleProperty dp = line.getStartX() < line.getEndX() ? line.startXProperty() : line.endXProperty();
        newLine.startXProperty().bind(dp);
        return newLine;
    }
    private IBoundaryRuler getVruler() {
        return (IBoundaryRuler)rulers.second();
    }
    //endregion Private Methods

    //region Create

    public FibLine(String ticker,
                   int location,
                   Line line,
                   Tuple<IRuler> rulers) {
        this.ticker = ticker;
        this.location = location;
        this.rulers = rulers;
        this.dragLine = new DraggableLine(line);
        setupDragLine();
    }



    public FibLine(String ticker,
                   int location,
                   Tuple<Double> p1,
                   Tuple<Double> p2,
                   Tuple<IRuler> rulers) {
        this.p1 = p1;
        this.p2 = p2;
        this.ticker = ticker;
        this.location = location;
        this.rulers = rulers;
    }

    public FibLine(ObjectId oid,
                   String ticker,
                   int location,
                   FinancialCoord fp1,
                   FinancialCoord fp2,
                   Tuple<IRuler> rulers) {
        this.oid = oid;
        this.ticker = ticker;
        this.location = location;
        this.fp1 = fp1;
        this.fp2 = fp2;
        this.rulers = rulers;
    }
    //endregion Create

    //region Interface ChartItem
    private Node _view;
    @Override
    public Node view() {
        if (_view == null) {
            if (dragLine == null) {
                if (p1 == null) {
                    IRuler hruler = rulers.first();
                    IRuler vruler = rulers.second();
                    double p1x = hruler.calcPix(fp1.getX());
                    double p1y = vruler.calcPix(fp1.getY());
                    double p2x = hruler.calcPix(fp2.getX());
                    double p2y = vruler.calcPix(fp2.getY());
                    dragLine = new DraggableLine(p1x,p1y,p2x,p2y);
                }
                else {
                    dragLine = new DraggableLine(p1.first(),p1.second(),p2.first(),p2.second());
                }
                setupDragLine();
            }
            _view = dragLine.view();
        }
        return _view;
    }

    private MaunaloaStatus status;
    @Override
    public MaunaloaStatus getStatus() {
        if (status == null) {
            return new MaunaloaStatus(StatusCodes.NA,
                                      dragLine.getStatusProperty());
        }
        return status;
    }


    /*
    @Override
    public int getStatus() {
        if (oid == null) {
            if (dragLine.getStatus() == ChartStatusCodes.SELECTED) {
                return ChartItem.SELECTED;
            }
            else {
                return ChartItem.NA;
            }
        }
        else {
            return ChartItem.NA;
        }
    }

    @Override
    public void setStatus(int value) {
        this.status = value;
    }*/
    //region Interface ChartItem
}
