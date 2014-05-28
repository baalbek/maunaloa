package maunaloa.controllers.helpers;

import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.SpotItem;
import oahu.financial.StockPrice;
import oahux.controllers.MaunaloaChartViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcs on 5/26/14.
 */
public class SpotHelper extends AbstractControllerHelper {
    private ChartItem spotItem;

    public SpotHelper(MaunaloaChartViewModel boss) {
        super(boss);
    }

    public void updateSpot(StockPrice spot) {
        if (spot == null) {
            spotItem = new SpotItem(spot,boss.getHruler(),boss.getVruler());
        }
        else {
            spotItem.removeFrom(boss.getPane().getChildren());
        }
        updateMyPaneLines(spotItem, lineMap());
    }
}
