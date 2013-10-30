package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import maunaloa.events.DerivativesControllerListener;
import maunaloa.events.MainFrameControllerListener;
import maunaloa.models.MaunaloaFacade;
import oahu.financial.Stock;
import oahux.chart.IDateBoundaryRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/27/13
 * Time: 2:18 PM
 */
public interface ChartCanvasController extends
                                        MaunaloaChartViewModel,
                                        DerivativesControllerListener,
                                        MainFrameControllerListener {
    void setTicker(Stock ticker);
    void setChart(MaunaloaChart chart);
    void setModel(MaunaloaFacade model);
    void setName(String name);
    void setLocation(int loc);
    int getLocation();
    BooleanProperty fibonacci1272extProperty();
    IDateBoundaryRuler getHruler();

}
