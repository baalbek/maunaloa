package maunaloa.charts.entities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.charts.ChartItem;
import maunaloa.charts.LevelLine;
import oahux.chart.IRuler;

import java.time.LocalDate;

/**
 * Created by rcs on 25.01.16.
 *
 */
public class LevelEntity implements ChartItem {
    private LevelLine level;
    private double levelValue;
    private IRuler<Double> ruler;

    public LevelEntity(Double value, IRuler<Double> ruler) {
        this.level = null;
        this.ruler = ruler;
    }

    //region Interface ChartItem
    private Node _view;
    @Override
    public Node view() {
        return null;
    }

    @Override
    public void updateRulers(IRuler<LocalDate> hruler, IRuler<Double> vruler) {

    }

    @Override
    public void removeFrom(ObservableList<Node> container) {

    }
    //endregion Interface ChartItem

}
