package maunaloa.views.charts;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.repository.WindowDressingRepository;
import oahu.financial.OptionCalculator;
import oahux.chart.IRuler;
import oahux.financial.DerivativeFx;

import java.util.List;
import java.util.Optional;

/**
 * Created by rcs on 12.09.15.
 *
 */


public class OptionPriceSlider implements ChartItem {
    private final LevelLine levelLine;
    private final DerivativeFx derivative;

    public OptionPriceSlider(DerivativeFx derivative, IRuler ruler) {
        this.derivative = derivative;
        this.levelLine = new LevelLine(derivative.getStockPrice().getValue(), ruler);
        this.levelLine.setValueLabelFunc((levelValue) -> {
            OptionCalculator calculator = derivative.getCalculator();
            double strike = derivative.getDerivative().getX();
            double t =  0.5; //derivative.getDerivative().getExpiry()
            double sigma = derivative.getIvBuy();
            double optionPrice = derivative.getDerivative().getOpType() == 1 ?
                calculator.callPrice(levelValue,strike,t,sigma) :
                calculator.putPrice(levelValue,strike,t,sigma);
            return String.format("Option price: %.2f", optionPrice);
        });
    }

    @Override
    public Node view() {
        return levelLine.view();
    }

    @Override
    public Optional<Node> commentsView() {
        return null;
    }

    @Override
    public void setEntityStatus(int value) {
    }

    @Override
    public MaunaloaStatus getStatus() {
        return null;
    }

    @Override
    public void saveToRepos(WindowDressingRepository repos) {
    }

    @Override
    public Optional<List<CommentEntity>> getComments() {
        return null;
    }

    @Override
    public void removeFrom(ObservableList<Node> container) {
    }
}
