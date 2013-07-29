package maunaloa.controllers;

import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;
import oahux.models.MaunaloaFacade;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/27/13
 * Time: 2:18 PM
 */
public interface ChartCanvasController extends MaunaloaChartViewModel {
    void setTicker(String ticker);
    //void draw();
    void setChart(MaunaloaChart chart);
    void setModel(MaunaloaFacade model);
}
