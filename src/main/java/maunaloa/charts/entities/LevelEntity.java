package maunaloa.charts.entities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.charts.ChartItem;
import maunaloa.charts.LevelLine;
import oahux.chart.IRuler;
import oahux.repository.ColorRepository;

import java.time.LocalDate;

/**
 * Created by rcs on 25.01.16.
 *
 */
public class LevelEntity implements ChartItem {
    private LevelLine levelLine;
    private double levelValue;
    private IRuler<Double> ruler;

    public LevelEntity(Double value, IRuler<Double> ruler, ColorRepository colorRepos) {
        this.ruler = ruler;
        levelLine = new LevelLine(levelValue, ruler, colorRepos);
        System.out.println("New LevelLine: " + levelLine);
    }

    //region Interface ChartItem
    //private Node _view;
    @Override
    public Node view() {
        /*
        if (_view == null) {
            levelLine.setOnMouseReleased((evt,anchor) -> {
                cleanStatusProperty().set(isClean());
            });
            levelLine.setOnMouseReleasedShift(evt -> {
                loadCommentsDialog();
            });
            _view = levelLine.view();
        }
        */
        return levelLine.view();
    }

    @Override
    public void updateRulers(IRuler<LocalDate> hruler, IRuler<Double> vruler) {

    }

    @Override
    public void removeFrom(ObservableList<Node> container) {

    }
    //endregion Interface ChartItem

}
