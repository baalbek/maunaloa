package maunaloa.controllers.helpers;

import javafx.scene.layout.Pane;
import oahu.financial.Stock;
import oahux.chart.IRuler;
import oahux.controllers.MaunaloaChartViewModel;

/**
 * Created by rcs on 5/4/14.
 */
public class LevelHelper extends AbstractControllerHelper {
    public LevelHelper(MaunaloaChartViewModel boss) {
        super(boss);
    }

    public void onNewLevel() {
        Stock stock = boss.getStock();
        if (stock == null) {
            return;
        }
        Pane myPane = boss.getPane();
        IRuler vruler = boss.getVruler();

    }
}
