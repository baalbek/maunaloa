package maunaloa.entities.windowdressing;

import javafx.scene.Node;
import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.DraggableLine;
import maunaloa.views.charts.FinancialCoord;
import oahu.domain.Tuple;
import oahux.chart.IRuler;
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
    private ObjectId oid;
    private final String ticker;
    private final int location;
    private FinancialCoord fp1, fp2;
    private Tuple<Double> p1,p2;

    private DraggableLine dragLine;
    private int status;

    public ObjectId getOid() {
        return oid;
    }
    public void setOid(ObjectId oid) {
        this.oid = oid;
    }

    public String getTicker() {
        return ticker;
    }

    public int getLocation() {
        return location;
    }
    //public FinancialCoord financialCoord()
    //endregion Properties

    //region Create

    public FibLine(String ticker,
                   int location,
                   Tuple<Double> p1,
                   Tuple<Double> p2,
                   MaunaloaChartViewModel viewModel) {
        this.p1 = p1;
        this.p2 = p2;
        this.viewModel = viewModel;
        this.ticker = ticker;
        this.location = location;
    }

    public FibLine(ObjectId oid,
                   String ticker,
                   int location,
                   FinancialCoord fp1,
                   FinancialCoord fp2,
                   MaunaloaChartViewModel viewModel) {
        this.oid = oid;
        this.ticker = ticker;
        this.location = location;
        this.fp1 = fp1;
        this.fp2 = fp2;
        this.viewModel = viewModel;
    }
    //endregion Create

    //region Interface ChartItem
    private Node _view;
    @Override
    public Node view() {
        if (_view == null) {
            if (p1 == null) {
                IRuler hruler = viewModel.getHruler();
                IRuler vruler = viewModel.getVruler();
                double p1x = hruler.calcPix(fp1.getX());
                double p1y = vruler.calcPix(fp1.getY());
                double p2x = hruler.calcPix(fp2.getX());
                double p2y = vruler.calcPix(fp2.getY());
                dragLine = new DraggableLine(p1x,p1y,p2x,p2y);
            }
            else {
                dragLine = new DraggableLine(p1.first(),p1.second(),p2.first(),p2.second());
            }
        }
        return _view;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int value) {
        this.status = value;
    }
    //region Interface ChartItem
}
