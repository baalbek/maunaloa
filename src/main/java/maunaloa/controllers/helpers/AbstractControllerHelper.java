package maunaloa.controllers.helpers;

import maunaloa.service.Logx;
import maunaloa.views.charts.ChartItem;
import oahu.financial.Stock;
import oahux.controllers.MaunaloaChartViewModel;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by rcs on 5/1/14.
 */
public abstract class AbstractControllerHelper {
    protected Logger log = Logger.getLogger(getClass().getPackage().getName());

    protected final MaunaloaChartViewModel boss;

    public AbstractControllerHelper(MaunaloaChartViewModel boss) {
        this.boss = boss;
    }

    protected void updateMyPaneLines(ChartItem line, Map<Stock,List<ChartItem>> linesMap) {
        if (linesMap == null) {
            log.warn("My lines was null");
            return;
        }
        Stock curTicker = boss.getStock();
        List<ChartItem> lines = linesMap.get(curTicker);
        if (lines == null) {
            lines = new ArrayList<>();
            linesMap.put(curTicker, lines);
        }
        lines.add(line);
        boss.getPane().getChildren().add(line.view());
        Logx.debug(log, () -> String.format("Added line %s to %s", line, curTicker.getTicker()));
    }

    protected void clearLines(Map<Stock,List<ChartItem>> myLines) {
        /*
        List<ChartItem> lines = myLines.get(boss.getStock());

        if (lines == null) {
            log.warn("My lines was null");
            return;
        }

        for (ChartItem l : lines) {
            boss.getPane().getChildren().remove(l.view());
            Logx.debug(log, () -> String.format("Removed line %s from %s",l, boss.getStock().getTicker()));
        }
        */
        processLines("Deleted", myLines, l -> {
            boss.getPane().getChildren().remove(l.view());
        });
    }
    protected void refreshLines(Map<Stock,List<ChartItem>> myLines) {
        processLines("Added", myLines, l -> {
            boss.getPane().getChildren().add(l.view());
        });
    }
    protected void processLines(String action,
                                Map<Stock,List<ChartItem>> myLines,
                                Consumer<ChartItem> fn) {
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
    }
}
