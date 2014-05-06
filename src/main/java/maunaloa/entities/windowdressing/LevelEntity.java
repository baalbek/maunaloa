package maunaloa.entities.windowdressing;

import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.LevelLine;
import oahux.chart.IRuler;
import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 13:35
 */
public class LevelEntity extends AbstractWindowDressingItem implements ChartItem {

    private final double levelValue;
    private final IRuler vruler;
    private LevelLine levelLine;

    public LevelEntity(ObjectId oid,
                       String ticker,
                       int location,
                       double levelValue,
                       IRuler vruler) {
        this(ticker,location,levelValue,vruler);
        this.oid = oid;
    }

    public LevelEntity(String ticker,
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
            levelLine = new LevelLine(levelValue, vruler);
            levelLine.setOnMouseReleased((evt,anchor) -> {
                statusProperty().set(getEntityStatus());
            });
            _view = levelLine.view();
        }
        return _view;
    }

    private MaunaloaStatus status;
    @Override
    public MaunaloaStatus getStatus() {
        if (status == null) {
            return new MaunaloaStatus(statusProperty(),
                                      levelLine.statusProperty());
        }
        return status;
    }
    //endregion Interface ChartItem

    //region Private/Protected
    protected int getEntityStatus() {
       return StatusCodes.ENTITY_CLEAN;
    }
    //endregion
}
