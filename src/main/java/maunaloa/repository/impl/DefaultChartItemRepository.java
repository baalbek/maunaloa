package maunaloa.repository.impl;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.charts.RiscLines;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.repository.ChartItemRepository;
import oahu.dto.Tuple2;
import oahu.financial.Stock;
import oahux.controllers.ControllerLocation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rcs on 17.01.16.
 *
 */
public class DefaultChartItemRepository implements ChartItemRepository {

    private Map<Tuple2<ControllerLocation,Stock>, List<RiscLines>> riscLines;

    @Override
    public void addRiscLines(ChartCanvasController controller, Stock stock, List<RiscLines> value) {
        if (riscLines == null) {
            riscLines = new HashMap<>();
        }
        ControllerLocation location = controller.getControllerLocation();
        Tuple2<ControllerLocation,Stock> key = new Tuple2<>(location,stock);
        List<RiscLines> lines = riscLines.get(key);
        if (lines == null) {
            lines = new ArrayList<>();
            riscLines.put(key, lines);
        }
        lines.addAll(value);
        ObservableList<Node> container = controller.getPane().getChildren();
        List<Node> nodes = value.stream().map(RiscLines::view).collect(Collectors.toList());
        container.addAll(nodes);
    }

    /*
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
    */
}
