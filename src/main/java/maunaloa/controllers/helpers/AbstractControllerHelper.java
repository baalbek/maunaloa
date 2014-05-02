package maunaloa.controllers.helpers;

import javafx.scene.Node;
import maunaloa.service.Logx;
import maunaloa.views.charts.ChartItem;
import oahu.financial.Stock;
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

    protected void clearLines(Map<Stock,List<ChartItem>> myLines) {
        processLines("Cleared", myLines, l -> {
            boss.getPane().getChildren().remove(l.view());
        }, null);
    }
    protected void deleteLines(Map<Stock,List<ChartItem>> myLines) {
        processLines("Deleted",
                myLines,
                l -> {
                    boss.getPane().getChildren().remove(l.view());
                },
                lx -> {
                    System.out.println("Postprocessing " + lx);
                });
    }
    protected void refreshLines(Map<Stock,List<ChartItem>> myLines) {
        processLines("Added", myLines, l -> {
            boss.getPane().getChildren().add(l.view());
        }, null);
    }
    protected void processLines(String action,
                                Map<Stock,List<ChartItem>> myLines,
                                Consumer<ChartItem> fn,
                                Consumer<List<ChartItem>> postProcess)
                                 {
        List<ChartItem> lines = myLines.get(boss.getStock());

        if (lines == null) {
            Stock s = boss.getStock();
            if (s != null) {
                log.warn(String.format("[%s] My lines was null. ", boss.getStock().getTicker()));
            }
            return;
        }
        for (ChartItem l : lines) {
            fn.accept(l);
            Logx.debug(log, () -> String.format("%s line %s from %s",action, l, boss.getStock().getTicker()));
        }
        if (postProcess != null) {
            postProcess.accept(lines);
        }
    }
}
