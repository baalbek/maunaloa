package maunaloa.entities.windowdressing;

import javafx.scene.Node;
import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.DraggableLine;
import maunaloa.views.charts.FinancialCoord;
import oahux.controllers.MaunaloaChartViewModel;
import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 13:34
 */
public class FibLine implements ChartItem {
    //region Properties
    private final MaunaloaChartViewModel viewModel;
    private final ObjectId oid;
    private final String ticker;
    private final int location;
    private final FinancialCoord p1, p2;
    private DraggableLine dragLine;

    public ObjectId getOid() {
        return oid;
    }

    public String getTicker() {
        return ticker;
    }

    public int getLocation() {
        return location;
    }
    //endregion Properties

    //region Create
    public FibLine(String ticker,
                   int location,
                   FinancialCoord p1,
                   FinancialCoord p2,
                   MaunaloaChartViewModel viewModel) {
        this(null,ticker,location,p1,p2,viewModel);
    }
    public FibLine(ObjectId oid,
                   String ticker,
                   int location,
                   FinancialCoord p1,
                   FinancialCoord p2,
                   MaunaloaChartViewModel viewModel) {
        this.oid = oid;
        this.ticker = ticker;
        this.location = location;
        this.p1 = p1;
        this.p2 = p2;
        this.viewModel = viewModel;
    }
    //endregion Create

    //region Interface ChartItem
    private Node _view;
    @Override
    public Node view() {
        if (_view == null) {
            dragLine = new DraggableLine(0,0,200,200);
        }
        return _view;
    }
    //region Interface ChartItem
}
