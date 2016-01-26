package maunaloa.repository;

import maunaloa.charts.RiscLines;
import maunaloa.charts.entities.LevelEntity;
import maunaloa.controllers.ChartCanvasController;
import oahu.financial.Stock;
import oahux.chart.IRuler;

import java.util.List;

/**
 * Created by rcs on 16.01.16.
 *
 */
public interface ChartItemRepository {
    void addRiscLines(ChartCanvasController controller, Stock stock, List<RiscLines> value);
    void removeLines(ChartCanvasController controller, Stock stock, ChartItemType cit);
    LevelEntity newLevelEntity(ChartCanvasController controller,
                               Stock stock,
                               IRuler<Double> ruler,
                               double levelValue);
    /*
    void addRiscLines(Stock stock, ControllerCategory location, RiscLines value);
    void removeRiscLines(Stock stock, ControllerCategory location);
    Optional<List<RiscLines>> getRiscLines(Stock stock, ControllerCategory location);
    */
}
