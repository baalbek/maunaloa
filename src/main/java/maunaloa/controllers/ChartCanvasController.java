package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import maunaloa.events.DerivativesControllerListener;
import oahu.financial.Stock;
import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;
import oahux.models.MaunaloaFacade;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/27/13
 * Time: 2:18 PM
 */
public interface ChartCanvasController extends MaunaloaChartViewModel, DerivativesControllerListener {
    void setTicker(Stock ticker);
    void setChart(MaunaloaChart chart);
    void setModel(MaunaloaFacade model);
    void setName(String name);
    void setMenus(Map<String,Menu> menus);
    BooleanProperty isFibonacciExtensionsProperty();
}
