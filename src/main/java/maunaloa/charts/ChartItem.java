package maunaloa.charts;

import javafx.scene.Node;
import oahux.chart.IRuler;

import java.time.LocalDate;

/**
 * Created by rcs on 16.01.16.
 *
 */
public interface ChartItem {
    Node view();
    void updateRulers(IRuler<LocalDate> hruler, IRuler<Double> vruler);
}
