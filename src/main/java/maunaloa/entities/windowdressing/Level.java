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
public class Level extends AbstractWindowDressingItem implements ChartItem {

    //region Create
    public Level(String ticker, int location) {
        super(ticker,location);
    }
    //endregion Create

    //region Interface ChartItem
    @Override
    public Node view() {
        throw new NotImplementedException();
    }

    @Override
    public MaunaloaStatus getStatus() {
        throw new NotImplementedException();
    }
    //endregion Interface ChartItem
}
