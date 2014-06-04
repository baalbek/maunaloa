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
    private final DerivativeFx derivative;
    private final IBoundaryRuler ruler;
    private Group group ;
    private MaunaloaStatus maunaloaStatus;

    public RiscLines(DerivativeFx derivative, IRuler ruler) {
        this.derivative = derivative;
        this.ruler = (IBoundaryRuler)ruler;

    }
    //region Interface ChartItem
    @Override
    public Node view() {
        if (group == null) {
            group = new Group();

            createLevel(derivative.getBreakeven(), Color.GREEN,
                    String.format("(%s) Break-even: %.2f",
                            derivative.getTicker(),
                            derivative.getBreakeven()));
            createLevel(derivative.stockPriceRiskProperty().get(), Color.RED,
                    String.format("(%s) Risc: %.2f",
                            derivative.getTicker(),
                            derivative.stockPriceRiskProperty().get()));

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
    private void createLevel(double value, Color lineColor, String text){
        Point2D pt0 = ruler.getUpperLeft();
        Point2D pt = ruler.getLowerRight();
        double yBe = ruler.calcPix(value);
        Line line = new Line(pt0.getX(),yBe,pt.getX(),yBe);
        line.setStroke(lineColor);
        group.getChildren().add(line);
        group.getChildren().add(new Text(pt0.getX()+20,yBe-8,text));
    }
    //endregion Private Methods
}
