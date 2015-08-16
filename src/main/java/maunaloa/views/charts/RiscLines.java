package maunaloa.views.charts;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.repository.WindowDressingRepository;
import oahu.domain.Tuple2;
import oahu.exceptions.NotImplementedException;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import oahux.financial.DerivativeFx;

import java.util.List;
import java.util.Optional;

/**
 * Created by rcs on 5/20/14.
 */
public class RiscLines implements ChartItem {
    private final DerivativeFx dpricefx;
    private IBoundaryRuler ruler;
    private Group group ;
    private MaunaloaStatus maunaloaStatus;

    private Tuple2<Line,Text> breakEvenLevel;
    private Tuple2<Line,Text> riscLevel;

    public RiscLines(DerivativeFx derivative, IRuler ruler) {
        this.dpricefx = derivative;
        this.ruler = (IBoundaryRuler)ruler;

    }
    public void updateRuler(IRuler vruler) {
        ruler = (IBoundaryRuler)vruler;

        double yBe = ruler.calcPix(dpricefx.getBreakeven());
        Line beLine = breakEvenLevel.first();
        beLine.setStartY(yBe);
        beLine.setEndY(yBe);
        Text beText = breakEvenLevel.second();
        beText.setY(yBe - 8);

        double yRisc = ruler.calcPix(dpricefx.stockPriceRiskProperty().get());
        Line riscLine = riscLevel.first();
        riscLine.setStartY(yRisc);
        riscLine.setEndY(yRisc);
        Text riscText = riscLevel.second();
        riscText.setY(yRisc - 8);
    }

    //region Interface ChartItem
    @Override
    public Node view() {
        if (group == null) {
            group = new Group();

            breakEvenLevel = createLevel(dpricefx.getBreakeven(), Color.GREEN,
                    String.format("(%s) Break-even: %.2f",
                            dpricefx.getDerivative().getTicker(),
                            dpricefx.getBreakeven()));
            riscLevel = createLevel(dpricefx.stockPriceRiskProperty().get(), Color.RED,
                    String.format("(%s) Risc: %.2f",
                            dpricefx.getDerivative().getTicker(),
                            dpricefx.stockPriceRiskProperty().get()));

        }
        return group;
    }

    @Override
    public Optional<Node> commentsView() {
        return Optional.empty();
    }

    @Override
    public void setEntityStatus(int value) {
        throw new NotImplementedException();
    }

    @Override
    public MaunaloaStatus getStatus() {
        if (maunaloaStatus == null) {
            IntegerProperty entStatus = new SimpleIntegerProperty(StatusCodes.NA);
            if (maunaloaStatus == null) {
                return new MaunaloaStatus(entStatus,null,null);
            }
        }
        return maunaloaStatus;
    }

    @Override
    public void saveToRepos(WindowDressingRepository repos) {
    }

    @Override
    public Optional<List<CommentEntity>> getComments() {
        return Optional.empty();
    }

    @Override
    public void removeFrom(ObservableList<Node> container) {
        if (group != null) {
            container.remove(group);
        }
    }
    //endregion Interface ChartItem

    //region Private Methods
    /*private void createLevels(DerivativeFx d, Color lineColor, String text){

    }*/
    private Tuple2<Line,Text> createLevel(double value, Color lineColor, String text){
        Point2D pt0 = ruler.getUpperLeft();
        Point2D pt = ruler.getLowerRight();
        double yBe = ruler.calcPix(value);
        Line line = new Line(pt0.getX(),yBe,pt.getX(),yBe);
        line.setStroke(lineColor);
        group.getChildren().add(line);
        Text textItem = new Text(pt0.getX()+20,yBe-8,text);
        group.getChildren().add(textItem);
        return new Tuple2<Line,Text> (line,textItem);
    }
    //endregion Private Methods
}
