package maunaloa.views.charts;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.repository.WindowDressingRepository;
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
        this.levelLine = new LevelLine(35, ruler);
        this.levelLine.setValueLabelFunc((levelValue) -> {
            return String.format("Blast: %.2f", derivative.getBuy() + levelValue);
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
/*
public class OptionPriceSlider extends LevelLine implements ChartItem {
    private final DerivativeFx derivative;
    public OptionPriceSlider(double levelValue, IRuler ruler) {
        super(levelValue, ruler);
        derivative = null;
    }
    public OptionPriceSlider(DerivativeFx derivative, double levelValue, IRuler ruler) {
        super(levelValue, ruler);
        this.derivative = derivative;
    }

    @Override
    protected String valueLabelText() {
        return "Hi";
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
        return Optional.empty();
    }

    @Override
    public void removeFrom(ObservableList<Node> container) {
    }
}
//*/
