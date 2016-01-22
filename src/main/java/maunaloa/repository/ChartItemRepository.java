package maunaloa.repository;

import maunaloa.charts.RiscLines;
import maunaloa.controllers.ChartCanvasController;
import oahu.financial.Stock;

import java.util.List;

/**
 * Created by rcs on 16.01.16.
 *
 */
public interface ChartItemRepository {
    void addRiscLines(ChartCanvasController controller, Stock stock, List<RiscLines> value);
    /*
    void addRiscLines(Stock stock, ControllerCategory location, RiscLines value);
    void removeRiscLines(Stock stock, ControllerCategory location);
    Optional<List<RiscLines>> getRiscLines(Stock stock, ControllerCategory location);
    */
}
