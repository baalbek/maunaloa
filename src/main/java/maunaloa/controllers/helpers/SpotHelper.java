package maunaloa.controllers.helpers;

import maunaloa.views.charts.ChartItem;
import oahu.financial.StockPrice;
import oahux.controllers.MaunaloaChartViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcs on 5/26/14.
 */
public class SpotHelper extends AbstractControllerHelper {
    private List<ChartItem> spots;

    public SpotHelper(MaunaloaChartViewModel boss) {
        super(boss);
    }

    public void updateSpot(StockPrice spot) {
        if (spots == null) {
            spots = new ArrayList<>();
        }
        else {
            for (ChartItem item : spots) {
                item.removeFrom(boss.getPane().getChildren());
            }
            spots.clear();
        }

    }
}
