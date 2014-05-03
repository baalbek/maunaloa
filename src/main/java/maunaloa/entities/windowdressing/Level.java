package maunaloa.entities.windowdressing;

import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.views.charts.ChartItem;
import oahu.exceptions.NotImplementedException;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 13:35
 */
public class Level implements ChartItem {
    @Override
    public Node view() {
        throw new NotImplementedException();
    }

    @Override
    public MaunaloaStatus getStatus() {
        throw new NotImplementedException();
    }
}
