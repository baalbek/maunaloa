package maunaloa.controllers;

import oahux.chart.MaunaloaChart;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/27/13
 * Time: 2:18 PM
 */
public interface ChartCanvasController {
    void draw();
    void setChart(MaunaloaChart chart);
}
