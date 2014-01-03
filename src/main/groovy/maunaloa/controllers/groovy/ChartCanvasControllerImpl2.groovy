package maunaloa.controllers.groovy

import javafx.beans.property.BooleanProperty
import maunaloa.controllers.ChartCanvasController
import maunaloa.events.DerivativesCalculatedEvent
import maunaloa.events.FibonacciEvent
import maunaloa.events.NewLevelEvent
import maunaloa.events.StockPriceAssignedEvent
import maunaloa.events.mongodb.FetchFromMongoDBEvent
import maunaloa.events.mongodb.SaveToMongoDBEvent
import maunaloa.models.MaunaloaFacade
import maunaloa.views.CanvasGroup
import oahu.financial.Stock
import oahu.financial.StockPrice
import oahux.chart.IDateBoundaryRuler
import oahux.chart.IRuler
import oahux.chart.MaunaloaChart

class ChartCanvasControllerImpl2 implements ChartCanvasController {

    @Override
    void setTicker(Stock ticker) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void setChart(MaunaloaChart chart) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void setModel(MaunaloaFacade model) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void setName(String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void setLocation(int loc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    int getLocation() {
        return 0  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    BooleanProperty fibonacci1272extProperty() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    IDateBoundaryRuler getHruler() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    List<CanvasGroup> getLines() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void notify(DerivativesCalculatedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void notify(StockPriceAssignedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void onFibonacciEvent(FibonacciEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void onFetchFromMongoDBEvent(FetchFromMongoDBEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void onSaveToMongoDBEvent(SaveToMongoDBEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void onNewLevelEvent(NewLevelEvent evt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    Collection<StockPrice> stockPrices(int period) {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    Stock getTicker() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    IRuler getVRuler() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void setVRuler(IRuler ruler) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    IRuler getHRuler() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void setHRuler(IRuler ruler) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}