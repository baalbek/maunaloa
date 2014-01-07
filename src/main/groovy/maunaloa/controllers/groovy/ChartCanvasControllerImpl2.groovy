package maunaloa.controllers.groovy

import javafx.beans.InvalidationListener
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
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

    @FXML private Canvas myCanvas
    @FXML private VBox myContainer
    @FXML private Pane myPane

    //region Init
    public void initialize() {
        InvalidationListener listener = new InvalidationListener() {
            @Override
            public void invalidated(javafx.beans.Observable arg0) {
                if (ticker == null) return
                chart.draw(myCanvas)
            }
        }

        myCanvas.widthProperty().bind(myContainer.widthProperty())
        myCanvas.heightProperty().bind(myContainer.heightProperty())

        myCanvas.widthProperty().addListener(listener)
        myCanvas.heightProperty().addListener(listener)
    }
    //endregion

    private BooleanProperty _fibonacci1272extProperty= new SimpleBooleanProperty(true)
    @Override
    BooleanProperty fibonacci1272extProperty() {
        return _fibonacci1272extProperty
    }

    @Override
    List<CanvasGroup> getLines() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    //region Events
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
        if (event.getLocation() != this.location) return
        switch  (event.getAction()) {
            case FibonacciEvent.NEW_LINE:
                activateFibonacci();
                break;
            case FibonacciEvent.DELETE_SEL_LINES:
                deleteLines(fibLines,false);
                break;
            case FibonacciEvent.DELETE_ALL_LINES:
                deleteLines(fibLines,true);
                break;
        }
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
    //endregion

    @Override
    Collection<StockPrice> stockPrices(int period) {
        return model.stockPrices(getTicker().getTicker(),-1);
    }

    //region Property ticker
    private Stock ticker
    @Override
    void setTicker(Stock ticker) {
/*        clearLines(fibLines);
        clearLines(riscLevels);*/
        this.ticker = ticker;
        chart.draw(myCanvas)
/*        draw();
        refreshLines(fibLines);
        refreshLines(riscLevels);*/
    }
    @Override
    Stock getTicker() {
        return ticker
    }
    //endregion

    IRuler vruler

    IRuler hruler

    int location

    String name

    MaunaloaFacade model

    MaunaloaChart chart

}