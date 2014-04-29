package maunaloa.controllers;

import maunaloa.repository.StockRepository;
import maunaloa.repository.WindowDressingRepository;
import oahu.domain.Tuple;
import oahux.chart.IRuler;
import oahux.controllers.MaunaloaChartViewModel;

/**
 * Created by rcs on 4/24/14.
 */
public interface ControllerHub {
    WindowDressingRepository getWindowDressingRepository();
    StockRepository getStockRepository();
}
