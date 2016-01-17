package maunaloa.repository;

import maunaloa.charts.RiscLines;
import oahu.financial.Stock;
import oahux.controllers.ControllerCategory;

import java.util.List;
import java.util.Optional;

/**
 * Created by rcs on 16.01.16.
 *
 */
public interface ChartItemRepository {
    void addRiscLines(Stock stock, ControllerCategory location, RiscLines value);
    Optional<List<RiscLines>> getRiscLines(Stock stock, ControllerCategory location);
}
