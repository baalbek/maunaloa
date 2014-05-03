package maunaloa.entities.windowdressing;

import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.LevelLine;
import oahu.exceptions.NotImplementedException;
import oahux.chart.IRuler;
import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 13:35
 */
public class Level extends AbstractWindowDressingItem implements ChartItem {

    private final double levelValue;
    private final IRuler vruler;

    public Level(ObjectId oid,
                 String ticker,
                 int location,
                 double levelValue,
                 IRuler vruler) {
        this(ticker,location,levelValue,vruler);
        this.oid = oid;
    }

    public Level(String ticker,
                 int location,
                 double levelValue,
                 IRuler vruler) {
        super(ticker,location);
        this.levelValue = levelValue;
        this.vruler = vruler;
    }
    //endregion Init

    //region Interface ChartItem
    private Node _view;
    @Override
    public Node view() {
        if (_view == null) {
            LevelLine levelLine = new LevelLine(levelValue, vruler);
            _view = levelLine.view();
        }
        return _view;
    }

    @Override
    public MaunaloaStatus getStatus() {
        throw new NotImplementedException();
    }
    //endregion Interface ChartItem
}
