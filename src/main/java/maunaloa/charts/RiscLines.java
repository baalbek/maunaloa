package maunaloa.charts;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import oahu.dto.Tuple2;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import oahux.financial.DerivativeFx;

import java.time.LocalDate;

/**
 * Created by rcs on 16.01.16.
 *
 */
public class RiscLines implements ChartItem {
    private Tuple2<Line,Text> breakEvenLevel;
    private Tuple2<Line,Text> riscLevel;
    private final DerivativeFx derivative;
    private IBoundaryRuler<Double> ruler;
    private Group group;

    public RiscLines(DerivativeFx derivative, IRuler<Double> ruler) {
        this.derivative = derivative;
        this.ruler = (IBoundaryRuler<Double>)ruler;
    }

    //region Interface ChartItem
    @Override
    public Node view() {
        if (group == null) {
            group = new Group();

            breakEvenLevel = createLevel(derivative.getBreakeven(), Color.GREEN,
                    String.format("(%s) Break-even: %.2f",
                            derivative.getDerivative().getTicker(),
                            derivative.getBreakeven()));
            group.getChildren().addAll(breakEvenLevel.first(), breakEvenLevel.second());

            riscLevel = createLevel(derivative.stockPriceRiskProperty().get(), Color.RED,
                    String.format("(%s) Risc: %.2f",
                            derivative.getDerivative().getTicker(),
                            derivative.stockPriceRiskProperty().get()));
            group.getChildren().addAll(riscLevel.first(), riscLevel.second());
        }
        return group;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateRulers(IRuler<LocalDate> hruler, IRuler<Double> vruler) {
        ruler = (IBoundaryRuler<Double>)vruler;

        double yBe = ruler.calcPix(derivative.getBreakeven());
        Line beLine = breakEvenLevel.first();
        beLine.setStartY(yBe);
        beLine.setEndY(yBe);
        Text beText = breakEvenLevel.second();
        beText.setY(yBe - 8);

        double yRisc = ruler.calcPix(derivative.stockPriceRiskProperty().get());
        Line riscLine = riscLevel.first();
        riscLine.setStartY(yRisc);
        riscLine.setEndY(yRisc);
        Text riscText = riscLevel.second();
        riscText.setY(yRisc - 8);
    }

    @Override
    public void removeFrom(ObservableList<Node> container) {
        if (group != null) {
            container.remove(group);
        }
    }
    //endregion Interface ChartItem

    //region Private Methods
    @SuppressWarnings("unchecked")
    private Tuple2<Line,Text> createLevel(double value, Color lineColor, String text){
        Point2D pt0 = ruler.getUpperLeft();
        Point2D pt = ruler.getLowerRight();
        System.out.println("Value: " + value);
        double yBe = ruler.calcPix(value);
        Line line = new Line(pt0.getX(),yBe,pt.getX(),yBe);
        line.setStroke(lineColor);
        Text textItem = new Text(pt0.getX()+20,yBe-8,text);
        System.out.println(textItem.getText());
        System.out.println("pt0: " + pt0);
        System.out.println("pt: " + pt);
        System.out.println("yBe: " + yBe);
        return new Tuple2<>(line, textItem);
    }
    //endregion Private Methods
}
