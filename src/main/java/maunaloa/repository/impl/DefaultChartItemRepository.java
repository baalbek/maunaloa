package maunaloa.repository.impl;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.charts.ChartItem;
import maunaloa.charts.RiscLines;
import maunaloa.charts.entities.LevelEntity;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.repository.ChartItemRepository;
import maunaloa.repository.ChartItemType;
import oahu.dto.Tuple2;
import oahu.financial.Stock;
import oahux.chart.IRuler;
import oahux.controllers.ControllerLocation;
import oahux.repository.ColorRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rcs on 17.01.16.
 *
 */
public class DefaultChartItemRepository implements ChartItemRepository {

    private Map<Tuple2<ControllerLocation,Stock>, List<RiscLines>> riscLines;
    private Map<Tuple2<ControllerLocation,Stock>, List<LevelEntity>> levelLines;
    private ColorRepository colorRepos;

    //region Properties
    public void setColorRepos(ColorRepository colorRepos) {
        this.colorRepos = colorRepos;
    }
    //endregion Properties

    //region Interface ChartItemRepository
    public LevelEntity newLevelEntity(ChartCanvasController controller,
                                      Stock stock,
                                      IRuler<Double> ruler,
                                      double levelValue) {
        if (levelLines == null) {
            levelLines = new HashMap<>();
        }
        LevelEntity ent = new LevelEntity(levelValue, ruler, colorRepos);
        addLine(controller,stock,levelLines,ent);
        return ent;
    }
    @Override
    public void addRiscLines(ChartCanvasController controller, Stock stock, List<RiscLines> value) {
        if (riscLines == null) {
            riscLines = new HashMap<>();
        }
        addLines(controller,stock,riscLines,value);
    }

    @Override
    public void removeLines(ChartCanvasController controller, Stock stock, ChartItemType cit) {
        switch (cit) {
            case RISC_LINES:
                removeOrHideLines(controller,stock,riscLines,true);
                break;
            case LEVEL_LINES:
                removeOrHideLines(controller,stock,levelLines,true);
                break;

        }
    }



    //endregion Interface ChartItemRepository


    //region Private Methods
    private <T extends ChartItem> void addLine(ChartCanvasController controller,
                                                Stock stock,
                                                Map<Tuple2<ControllerLocation,Stock>,List<T>> linesMap,
                                                T value) {
        Tuple2<ControllerLocation,Stock> key = createKey(controller,stock);
        List<T> lines = linesMap.get(key);
        if (lines == null) {
            lines = new ArrayList<>();
            linesMap.put(key, lines);
        }
        lines.add(value);
        ObservableList<Node> container = controller.getPane().getChildren();
        container.add(value.view());
    }
    private <T extends ChartItem> void addLines(ChartCanvasController controller,
                                                Stock stock,
                                                Map<Tuple2<ControllerLocation,Stock>,List<T>> linesMap,
                                                List<T> value) {
        Tuple2<ControllerLocation,Stock> key = createKey(controller,stock);
        List<T> lines = linesMap.get(key);
        if (lines == null) {
            lines = new ArrayList<>();
            linesMap.put(key, lines);
        }
        lines.addAll(value);
        ObservableList<Node> container = controller.getPane().getChildren();
        List<Node> nodes = value.stream().map(T::view).collect(Collectors.toList());
        container.addAll(nodes);
    }
    private <T extends ChartItem> void removeOrHideLines(ChartCanvasController controller,
                                                         Stock stock,
                                                         Map<Tuple2<ControllerLocation,Stock>,List<T>> linesMap,
                                                         boolean deleteLines) {
        findLinesWithKey(controller,stock,linesMap).ifPresent(x -> {
            ObservableList<Node> container = controller.getPane().getChildren();
            x.lines.forEach(l -> {
                l.removeFrom(container);
            });
            if (deleteLines) {
                linesMap.remove(x.key);
            }
        });
    }
    private <T> Optional<LinesWithKey<T>> findLinesWithKey(
            ChartCanvasController controller,
            Stock stock,
            Map<Tuple2<ControllerLocation,Stock>,List<T>> linesMap
    ) {
        if (linesMap == null) {
            return Optional.empty();
        }
        Tuple2<ControllerLocation,Stock> key = createKey(controller,stock);
        List<T> lines = linesMap.get(key);
        if (lines == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(new LinesWithKey<T>(lines,key));
        }
    }

    private Tuple2<ControllerLocation,Stock> createKey(ChartCanvasController controller,
                                                       Stock stock) {
        return new Tuple2<>(controller.getControllerLocation(), stock);
    }

    //endregion Private Methods
}

class LinesWithKey<T> {
    public final List<T> lines;
    public final Tuple2<ControllerLocation,Stock> key;
    LinesWithKey(List<T> lines, Tuple2<ControllerLocation,Stock> key) {
        this.lines = lines;
        this.key = key;
    }
}
