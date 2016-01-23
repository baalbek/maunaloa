package maunaloa.repository.impl;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.charts.ChartItem;
import maunaloa.charts.RiscLines;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.repository.ChartItemRepository;
import maunaloa.repository.ChartItemType;
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

    //region Interface ChartItemRepository
    @Override
    public void addRiscLines(ChartCanvasController controller, Stock stock, List<RiscLines> value) {
        if (riscLines == null) {
            riscLines = new HashMap<>();
        }
        Tuple2<ControllerLocation,Stock> key = createKey(controller,stock);
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

    @Override
    public void removeLines(ChartCanvasController controller, Stock stock, ChartItemType cit) {
        switch (cit) {
            case RISC_LINES:
                removeLines(controller,stock,riscLines);
                break;
        }
    }

    public <T extends ChartItem> void removeLines(ChartCanvasController controller,
                                    Stock stock,
                                    Map<Tuple2<ControllerLocation,Stock>,List<T>> linesMap) {
        if (linesMap == null) {
            return;
        }
        Tuple2<ControllerLocation,Stock> key = createKey(controller,stock);
        List<T> lines = linesMap.get(key);
        if (lines == null) {
            return;
        }
        ObservableList<Node> container = controller.getPane().getChildren();

        lines.forEach(l -> {
            l.removeFrom(container);
        });
        linesMap.remove(key);
        /*
        if (riscLines == null) {
            return;
        }
        Tuple2<ControllerLocation,Stock> key = createKey(controller,stock);
        List<RiscLines> lines = riscLines.get(key);
        if (lines == null) {
            return;
        }

        ObservableList<Node> container = controller.getPane().getChildren();

        lines.forEach(l -> {
            l.removeFrom(container);
        });
        riscLines.remove(key);
        */
    }
    //endregion Interface ChartItemRepository


    //region Private Methods
    private Tuple2<ControllerLocation,Stock> createKey(ChartCanvasController controller,
                                                       Stock stock) {
        return new Tuple2<>(controller.getControllerLocation(), stock);
    }
    //endregion Private Methods

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
