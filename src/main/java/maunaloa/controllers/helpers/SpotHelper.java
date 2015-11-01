package maunaloa.controllers.helpers;

import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.SpotItem;
import oahu.dto.Tuple;
import oahu.dto.Tuple2;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.controllers.MaunaloaChartViewModel;

import java.time.LocalDate;

/**
 * Created by rcs on 5/26/14.
 */
public class SpotHelper extends AbstractControllerHelper {
    private ChartItem spotItem;

    public SpotHelper(MaunaloaChartViewModel boss) {
        super(boss);
    }

    public void updateSpot(StockPrice spot) {
        /*if (spotItem == null) {
            spotItem = new SpotItem(spot,boss.getHruler(),boss.getVruler());
        }
        else {
            spotItem.removeFrom(boss.getPane().getChildren());
            spotItem = new SpotItem(spot,boss.getHruler(),boss.getVruler());
        }*/
        if (spotItem != null) {
            spotItem.removeFrom(boss.getPane().getChildren());
        }
        spotItem = new SpotItem(spot,boss.getHruler(),boss.getVruler());
        updateMyPaneLines(spotItem, lineMap());
    }
    public void updateRulers(Tuple2<IRuler<LocalDate>,IRuler<Double>> rulers) {

    }
}
