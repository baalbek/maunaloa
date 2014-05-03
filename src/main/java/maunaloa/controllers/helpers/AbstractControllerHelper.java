package maunaloa.controllers.helpers;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.StatusCodes;
import maunaloa.service.Logx;
import maunaloa.views.charts.ChartItem;
import oahu.domain.Tuple2;
import oahu.financial.Stock;
import oahu.functional.Procedure2;
import oahux.controllers.MaunaloaChartViewModel;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by rcs on 5/1/14.
 */
public abstract class AbstractControllerHelper {
    protected Logger log = Logger.getLogger(getClass().getPackage().getName());

    protected final MaunaloaChartViewModel boss;

    public AbstractControllerHelper(MaunaloaChartViewModel boss) {
        this.boss = boss;
    }

    protected void updateMyPaneLines(List<ChartItem> newLines, Map<Stock,List<ChartItem>> linesMap) {
        getOrCreateEntry("lines from repos",linesMap).ifPresent(lines -> {
            lines.addAll(newLines);
            List<Node> chartItems = newLines.stream().map(ChartItem::view).collect(Collectors.toList());
            boss.getPane().getChildren().addAll(chartItems);
        });
    }
    protected void updateMyPaneLines(ChartItem line, Map<Stock,List<ChartItem>> linesMap) {
        getOrCreateEntry("line",linesMap).ifPresent(lines -> {
            lines.add(line);
            boss.getPane().getChildren().add(line.view());
        });
    }

    private Optional<List<ChartItem>> getOrCreateEntry(String msg, Map<Stock,List<ChartItem>> linesMap) {
        if (linesMap == null) {
            log.warn("My lines was null!");
            return Optional.empty();
        }

        Stock stock = boss.getStock();

        if (stock == null) {
            log.warn("Stock was null!");
            return Optional.empty();
        }
        List<ChartItem> curLines = linesMap.get(stock);
        if (curLines == null) {
            curLines = new ArrayList<>();
            linesMap.put(stock, curLines);
            Logx.debug(log, () -> String.format("Added %s to %s", msg, stock.getTicker()));
        }
        return Optional.of(curLines);
    }

    protected void deleteSelectedLines(Map<Stock,List<ChartItem>> chartItemsMap) {
        processWith(chartItemsMap, (curlines,s) -> {
            ObservableList<Node> container = boss.getPane().getChildren();
            List<ChartItem> selectedLines =
                    curlines.stream().
                    filter(l -> l.getStatus().getChartLineStatus() == StatusCodes.SELECTED).
                    collect(Collectors.toList());
            selectedLines.forEach(l -> {
                container.remove(l.view());
                curlines.remove(l);
            });
        });
    }

    protected void deleteAllLines(Map<Stock,List<ChartItem>> chartItemsMap) {
        processWith(chartItemsMap, (curlines,s) -> {
            ObservableList<Node> container = boss.getPane().getChildren();
            curlines.forEach(l -> {
                container.remove(l.view());
                Logx.debug(log, () -> String.format("Deleted line %s from %s", l, s.getTicker()));
            });
            chartItemsMap.remove(s);
        });
        /*Logx.debug(log, () -> {
            StringBuffer buf = new StringBuffer("Remaining entries:").append("\n");
            for (Stock s : chartItemsMap.keySet()) {
                buf.append(s.getTicker()).append("\n");
            }
            return buf.toString();
        });*/
    }

    protected void clearLines(Map<Stock,List<ChartItem>> chartItemsMap) {
        processWith(chartItemsMap, (curlines,s) -> {
            ObservableList<Node> container = boss.getPane().getChildren();
            curlines.forEach(l -> {
                container.remove(l.view());
                Logx.debug(log, () -> String.format("Cleared line %s from %s", l, s.getTicker()));
            });
        });
    }
    protected void refreshLines(Map<Stock,List<ChartItem>> chartItemsMap) {
        processWith(chartItemsMap, (curlines,s) -> {
            ObservableList<Node> container = boss.getPane().getChildren();
            curlines.forEach(l -> {
                container.add(l.view());
                Logx.debug(log, () -> String.format("Refreshed line %s from %s", l, s.getTicker()));
            });
        });

    }
    private void processWith(Map<Stock, List<ChartItem>> myLines,
                             Procedure2<List<ChartItem>,Stock> fn) {
        Stock s = boss.getStock();
        if (s == null) {
            log.warn("Stock was null!");
            return;
        }
        List<ChartItem> curlines = myLines.get(s);
        if (curlines == null) {

            if (s != null) {
                log.warn(String.format("[%s] My lines was null. ", boss.getStock().getTicker()));
            }
            return;
        }
        fn.apply(curlines,s);
    }
}
