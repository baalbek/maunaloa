package maunaloa.repository.impl;

import maunaloa.charts.RiscLines;
import maunaloa.repository.ChartItemRepository;
import oahu.dto.Tuple2;
import oahu.financial.Stock;
import oahux.controllers.ControllerCategory;

import java.util.*;

/**
 * Created by rcs on 17.01.16.
 *
 */
public class DefaultChartItemRepository implements ChartItemRepository {

    private Map<Tuple2<Stock,ControllerCategory>, List<RiscLines>> riscLines;

    //region Interface ChartItemRepository
    @Override
    public void addRiscLines(Stock stock, ControllerCategory location, RiscLines value) {
        if (riscLines == null) {
            riscLines = new HashMap<>();
        }
        Tuple2<Stock,ControllerCategory> key = new Tuple2<>(stock,location);
        List<RiscLines> lines = riscLines.get(key);
        if (lines == null) {
            lines = new ArrayList<>();
            riscLines.put(key, lines);
        }
        lines.add(value);
    }

    @Override
    public Optional<List<RiscLines>> getRiscLines(Stock stock, ControllerCategory location) {

        if (riscLines == null) {
            return Optional.empty();
        }

        List<RiscLines> lines = riscLines.get(new Tuple2<>(stock,location));

        if (lines == null) {
            return Optional.empty();
        }

        return Optional.of(lines);
    }
    //endregion Interface ChartItemRepository

    //region Private Methods
    //endregion Private Methods
}
