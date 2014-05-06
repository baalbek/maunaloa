package maunaloa.controllers.helpers;

import maunaloa.entities.windowdressing.LevelEntity;
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

}
