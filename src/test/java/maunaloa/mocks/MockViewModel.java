package maunaloa.mocks;

import javafx.geometry.Point2D;
import maunaloax.views.chart.DefaultDateRuler;
import maunaloax.views.chart.DefaultVRuler;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.controllers.MaunaloaChartViewModel;

import java.util.Collection;

/**
 * Created by rcs on 4/26/14.
 */
public class MockViewModel implements MaunaloaChartViewModel {
    private IRuler vruler;
    private IRuler hruler;

    @Override
    public Collection<StockPrice> stockPrices(int period) {
        throw new NotImplementedException();
    }

    @Override
    public Stock getStock() {
        throw new NotImplementedException();
    }

    @Override
    public IRuler getVruler() {
        if (vruler == null) {
            vruler = new DefaultVRuler(new Point2D(0,0), new Point2D(200,200),1,1000);
        }
        return vruler;
    }

    @Override
    public void setVruler(IRuler ruler) {
        throw new NotImplementedException();
    }

    @Override
    public IRuler getHruler() {
        if (hruler == null) {
            int y = 2014 - 1900;
            hruler = new DefaultDateRuler(0,
                    new java.util.Date(y,0,1),
                    new java.util.Date(y,3,1),
                    1.0,
                    1);
        }
        return hruler;
    }

    @Override
    public void setHruler(IRuler ruler) {
        throw new NotImplementedException();
    }
}
