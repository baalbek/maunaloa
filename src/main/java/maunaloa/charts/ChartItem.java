package maunaloa.charts;

import javafx.scene.Node;
import oahux.chart.IRuler;

/**
 * Created by rcs on 16.01.16.
 *
 */
public interface ChartItem {
    Node view();
    void updateRulers(IRuler hruler, IRuler vruler);
}
