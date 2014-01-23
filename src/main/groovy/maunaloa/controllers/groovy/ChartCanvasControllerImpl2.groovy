package maunaloa.controllers.groovy

import javafx.beans.InvalidationListener
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import maunaloa.controllers.ChartCanvasController
import maunaloa.events.DerivativesCalculatedEvent
import maunaloa.events.ChartCanvasLineEvent
import maunaloa.events.NewLevelEvent
import maunaloa.events.StockPriceAssignedEvent
import maunaloa.events.mongodb.FetchFromMongoDBEvent
import maunaloa.events.mongodb.SaveToMongoDBEvent
import maunaloa.models.MaunaloaFacade
import maunaloa.views.CanvasGroup
import oahu.financial.Stock
import oahu.financial.StockPrice
import oahux.chart.IRuler
import oahux.chart.MaunaloaChart
import org.apache.log4j.Logger

class ChartCanvasControllerImpl2 implements ChartCanvasController {

    @FXML private Canvas myCanvas
    @FXML private VBox myContainer
    @FXML Pane myPane

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

        fibController = new FibonacciController(parent: this)
        levelsController = new LevelsController(parent: this)
    }
    //endregion

    private void clearLines(Map<Stock,List<CanvasGroup>> linesMap) {
/*        List<CanvasGroup> lines = linesMap.get(getTicker())

        if (lines == null) return

        for (CanvasGroup l : lines) {
            myPane.getChildren().remove(l.view())
        }*/
    }

    private void refreshLines(Map<Stock,List<CanvasGroup>> linesMap) {
/*        List<CanvasGroup> lines = linesMap.get(getTicker())

        if (lines == null) return

        for (CanvasGroup l : lines) {
            myPane.getChildren().add(l.view())
        }
 */   }

    @Override
    List<CanvasGroup> getLines() {
        //return fibLines.get(getTicker())
        return null
    }

    //region Events
    @Override
    void notify(DerivativesCalculatedEvent event) {
        /*deleteLines(riscLevels,true)
        List<CanvasGroup> lines = new ArrayList<>()
        for(DerivativeFx d : event.getCalculated()) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("notify adding line for derivative %s",d.getTicker()))
            }
            lines.add(new RiscLines(d, vruler))
        }
        riscLevels.put(getTicker(), lines)
        refreshLines(riscLevels)*/
    }

    @Override
    void notify(StockPriceAssignedEvent event) {
        StockPrice sp = event.getStockPrice()
    }

    @Override
    void onChartCanvasLineEvent(ChartCanvasLineEvent event) {
        if (event.getLocation() != this.location) return
        switch  (event.getAction()) {
            case ChartCanvasLineEvent.NEW_LINE:
                fibController.activateFibonacci()
                break
            case ChartCanvasLineEvent.DELETE_SEL_LINES:
                fibController.deleteLines(false)
                levelsController.deleteLines(false)
                break
            case ChartCanvasLineEvent.DELETE_ALL_LINES:
                fibController.deleteLines(true)
                levelsController.deleteLines(true)
                break
        }
    }

    @Override
    void onFetchFromMongoDBEvent(FetchFromMongoDBEvent event) {
        fibController.onFetchFromMongoDBEvent(event)
        levelsController.onFetchFromMongoDBEvent(event)
    }

    @Override
    void onSaveToMongoDBEvent(SaveToMongoDBEvent event) {
        if (event.getLocation() != this.location) return
        fibController.onSaveToMongoDBEvent(event)
        levelsController.onSaveToMongoDBEvent(event)

        /*if (event.getLocation() != this.location) return

        List<CanvasGroup> lines = fibLines.get(getTicker())
        lines.each  { CanvasGroup line ->
            MongodbLine mongoLine = (MongodbLine)line
            MongoDBResult result = mongoLine.save(this)
            if (result.isOk()) {
                log.info(String.format("(%s) Successfully saved fibline with _id: %s to location: %d",
                        getTicker().getTicker(),
                        result.getObjectId(),
                        location))
            }
            else {
                log.error(String.format("(Saving fibline %s, %d) %s",tix,location,result.getWriteResult().getError()))
            }
        }*/
    }

    @Override
    void onNewLevelEvent(NewLevelEvent evt) {
        if (evt.getLocation() != this.location) return
        levelsController.onNewLevelEvent(evt)
    }
    //endregion

    @Override
    Collection<StockPrice> stockPrices(int period) {
        return model.stockPrices(getTicker().getTicker(),-1)
    }

    //region Property ticker
    private Stock ticker
    @Override
    void setTicker(Stock ticker) {
        fibController.clearLines()
        levelsController.clearLines()
        this.ticker = ticker
        chart.draw(myCanvas)
        fibController.refreshLines()
        levelsController.refreshLines()
    }

    @Override
    Stock getTicker() {
        return ticker
    }

    @Override
    public void setChart(MaunaloaChart chart) {
        this.chart = chart
        this.chart.setViewModel(this)
    }

//endregion

    IRuler vruler

    IRuler hruler

    long location

    String name

    MaunaloaFacade model

    private MaunaloaChart chart
    private Logger log = Logger.getLogger(getClass().getPackage().getName())
    private FibonacciController fibController = null
    private LevelsController levelsController = null
}