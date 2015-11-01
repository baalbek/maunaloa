package maunaloa.controllers.helpers;

import javafx.scene.layout.Pane;
import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.DateLine;
import oahu.dto.Tuple;
import oahu.dto.Tuple2;
import oahu.financial.Stock;
import oahux.chart.IRuler;
import oahux.controllers.MaunaloaChartViewModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by rcs on 30.10.15.
 *
 */
public class DateLineHelper extends AbstractControllerHelper {
    public DateLineHelper(MaunaloaChartViewModel boss) {
        super(boss);
    }
    public void onNewDateLine() {
        Stock stock = boss.getStock();
        if (stock != null) {
            Pane myPane = boss.getPane();
            myPane.setOnMouseReleased(e -> {
                IRuler vruler = boss.getVruler();
                IRuler hruler = boss.getHruler();
                double snapX = hruler.snapTo(e.getX());
                LocalDate dateX = (LocalDate)hruler.calcValue(snapX);
                System.out.println(dateX);
                DateLine dateLine = new DateLine(dateX, hruler, vruler);
                updateMyPaneLines(dateLine,lineMap());
                /*
                double x = ((LocalDate)hruler.calcValue(e.getX())).doubleValue();
                double snapX = hruler.snapTo(x);

                //*/
                myPane.setOnMouseReleased(null);
            });
        }
    }

    public void updateRulers(Tuple2<IRuler<LocalDate>,IRuler<Double>> rulers) {
        IRuler vruler = rulers.second();
        lineMap().forEach((Stock key, List<ChartItem> values) -> {
            values.forEach((ChartItem x) -> {
                DateLine ent = (DateLine)x;
                ent.updateRulers(rulers);
            });
        });
    }
}
