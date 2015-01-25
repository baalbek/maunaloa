package maunaloa.controllers.helpers;

import javafx.scene.layout.Pane;
import maunaloa.entities.windowdressing.LevelEntity;
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

    public void addNewLevel(LevelEntity entity) {
        updateMyPaneLines(entity, lineMap());
    }

    //region Events
    public void onNewLevel() {
        Stock stock = boss.getStock();
        if (stock != null) {
            Pane myPane = boss.getPane();
            myPane.setOnMouseReleased(e -> {
                IRuler vruler = boss.getVruler();

                double value = ((Double)vruler.calcValue(e.getY())).doubleValue();
                LevelEntity entity =
                        new LevelEntity(stock.getTicker(),
                                boss.getLocation(),
                                value,
                                boss.getVruler());
                addNewLevel(entity);
                myPane.setOnMouseReleased(null);
            });
        }
    }
    //endregion Events
}
