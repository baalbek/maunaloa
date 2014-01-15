package maunaloa.controllers.groovy

import com.mongodb.DBObject
import com.mongodb.WriteResult
import javafx.beans.InvalidationListener
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Line
import maunaloa.controllers.ChartCanvasController
import maunaloa.events.DerivativesCalculatedEvent
import maunaloa.events.FibonacciEvent
import maunaloa.events.NewLevelEvent
import maunaloa.events.StockPriceAssignedEvent
import maunaloa.events.mongodb.FetchFromMongoDBEvent
import maunaloa.events.mongodb.SaveToMongoDBEvent
import maunaloa.models.MaunaloaFacade
import maunaloa.views.CanvasGroup
import maunaloa.views.FibonacciDraggableLine
import maunaloa.views.Level
import maunaloa.views.MongodbLine
import maunaloa.views.RiscLines
import maunaloax.domain.MongoDBResult
import oahu.financial.Stock
import oahu.financial.StockPrice
import oahux.chart.IRuler
import oahux.chart.MaunaloaChart
import oahux.domain.DerivativeFx
import org.apache.log4j.Logger
import org.bson.types.ObjectId

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

    private void clearLines(Map<Stock,List<CanvasGroup>> linesMap) {
        List<CanvasGroup> lines = linesMap.get(getTicker())

        if (lines == null) return

        for (CanvasGroup l : lines) {
            myPane.getChildren().remove(l.view())
        }
    }

    private void refreshLines(Map<Stock,List<CanvasGroup>> linesMap) {
        List<CanvasGroup> lines = linesMap.get(getTicker())

        if (lines == null) return

        for (CanvasGroup l : lines) {
            myPane.getChildren().add(l.view())
        }
    }

    private void deleteLines(Map<Stock,List<CanvasGroup>> linesMap, boolean deleteAll) {
        List<CanvasGroup> lines = linesMap.get(getTicker())

        if (lines == null) return

        List<CanvasGroup> linesToBeRemoved = new ArrayList<>()

        for (CanvasGroup l : lines) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Will attempt to delete line %s with status %d", l, l.getStatus()))
            }
            if ((deleteAll == true) || (l.getStatus() == CanvasGroup.SELECTED)) {
                myPane.getChildren().remove(l.view())
                linesToBeRemoved.add(l)
            }
        }

        for (CanvasGroup l : linesToBeRemoved) {
            lines.remove(l)
        }

        //lines.clear();
    }

    private void updateMyPaneLines(CanvasGroup line, Map<Stock,List<CanvasGroup>> linesMap) {
        List<CanvasGroup> lines = linesMap.get(getTicker())
        if (lines == null) {
            lines = new ArrayList<>()
            linesMap.put(getTicker(), lines)
        }
        lines.add(line)
        myPane.getChildren().add(line.view())
    }

    @Override
    List<CanvasGroup> getLines() {
        return fibLines.get(getTicker())
    }

    //region Events
    @Override
    void notify(DerivativesCalculatedEvent event) {
        deleteLines(riscLevels,true)
        List<CanvasGroup> lines = new ArrayList<>()
        for(DerivativeFx d : event.getCalculated()) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("notify adding line for derivative %s",d.getTicker()))
            }
            lines.add(new RiscLines(d, vruler))
        }
        riscLevels.put(getTicker(), lines)
        refreshLines(riscLevels)
    }

    @Override
    void notify(StockPriceAssignedEvent event) {
        StockPrice sp = event.getStockPrice()
    }

    @Override
    void onFibonacciEvent(FibonacciEvent event) {
        final ObjectProperty<Line> lineA = new SimpleObjectProperty<>()
        def activateFibonacci = {
            myPane.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent evt) {
                    double x = getHruler().snapTo(evt.getX())
                    double y = evt.getY()
                    Line line = new Line(x, y, x, y)
                    myPane.getChildren().add(line)
                    lineA.set(line)
                }
            })
            myPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent evt) {
                    Line line = lineA.get()
                    if (line != null) {
                        line.setEndX(evt.getX())
                        line.setEndY(evt.getY())
                    }
                }
            })
            myPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent evt) {
                    Line line = lineA.get()
                    if (line != null) {
                        myPane.getChildren().remove(line)
                        if (log.isDebugEnabled()) {
                            log.debug(String.format("Has Fibonacci extension: %s", fibonacci1272extProperty().get()))
                        }
                        line.setStartX(getHruler().snapTo(line.getStartX()))
                        line.setEndX(getHruler().snapTo(line.getEndX()))
                        final CanvasGroup fibLine = new FibonacciDraggableLine(line,
                                getHruler(),
                                getVruler(),
                                fibonacci1272extProperty().get())
                        updateMyPaneLines(fibLine,fibLines)
                    }
                    lineA.set(null)
                    myPane.setOnMousePressed(null)
                    myPane.setOnMouseDragged(null)
                    myPane.setOnMouseReleased(null)
                }
            })
        }

        if (event.getLocation() != this.location) return
        switch  (event.getAction()) {
            case FibonacciEvent.NEW_LINE:
                activateFibonacci()
                break
            case FibonacciEvent.DELETE_SEL_LINES:
                deleteLines(fibLines,false)
                break
            case FibonacciEvent.DELETE_ALL_LINES:
                deleteLines(fibLines,true)
                break
        }
    }

    @Override
    void onFetchFromMongoDBEvent(FetchFromMongoDBEvent event) {
        def createLineFromDBObject = { DBObject obj ->
            DBObject p1 = (DBObject)obj.get("p1")
            DBObject p2 = (DBObject)obj.get("p2")

            double p1x = hruler.calcPix(p1.get("x"))
            double p1y = vruler.calcPix(p1.get("y"))

            double p2x = hruler.calcPix(p2.get("x"))
            double p2y = vruler.calcPix(p2.get("y"))

            Line line = new Line()
            line.setStartX(p1x)
            line.setStartY(p1y)
            line.setEndX(p2x)
            line.setEndY(p2y)
            return line
        }
        for (DBObject o : event.getLines()) {
            Line line = createLineFromDBObject(o)
            MongodbLine fibLine = new FibonacciDraggableLine(line,
                    getHruler(),
                    getVruler(),
                    fibonacci1272extProperty().get())

            fibLine.setMongodbId((ObjectId)o.get("_id"))

            updateMyPaneLines((CanvasGroup) fibLine, fibLines)
            println o
        }
    }

    @Override
    void onSaveToMongoDBEvent(SaveToMongoDBEvent event) {
        if (event.getLocation() != this.location) return

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
        }
        /*
        lines.each  { CanvasGroup line ->
            if (line.getStatus() == CanvasGroup.SELECTED) {
                MongodbLine mongoLine = (MongodbLine)line
                DBObject p1 = mongoLine.coord(MongodbLine.P1)
                DBObject p2 = mongoLine.coord(MongodbLine.P2)
                String tix = getTicker().getTicker()
                MongoDBResult result = model.getWindowDressingModel().saveFibonacci(tix,location,p1,p2)
                if (result.isOk()) {
                    log.info(String.format("(%s) Successfully saved fibline with _id: %s to location: %d",
                            tix,
                            result.getObjectId(),
                            location))
                    mongoLine.setMongodbId(result.getObjectId())
                    line.setStatus(CanvasGroup.SAVED_TO_DB)
                }
                else {
                    log.error(String.format("(Saving fibline %s, %d) %s",tix,location,result.getWriteResult().getError()))
                }
            }
            else if (line.getStatus() == CanvasGroup.SAVED_TO_DB_SELECTED) {
                MongodbLine mongoLine = (MongodbLine)line
                DBObject p1 = mongoLine.coord(MongodbLine.P1)
                DBObject p2 = mongoLine.coord(MongodbLine.P2)
                String tix = getTicker().getTicker()

                WriteResult result = model.getWindowDressingModel().updateCoord(mongoLine.getMongodbId(),p1,p2)
                if (result.getLastError().ok()) {
                    log.info(String.format("(%s) Successfully updated fibline with _id: %s to location: %d",
                            tix,
                            mongoLine.getMongodbId(),
                            location))
                    line.setStatus(CanvasGroup.SAVED_TO_DB)
                }
                else {
                    log.error(String.format("(Updating fibline %s, %d) %s",tix,location,result.getError()))
                }
            }
        }
        //*/
    }

    @Override
    void onNewLevelEvent(NewLevelEvent evt) {
        if (evt.getLocation() != this.location) return
        Level level = new Level(evt.getValue(), getVruler())
        updateMyPaneLines(level, levels)
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
        clearLines(fibLines)
        clearLines(riscLevels)
        this.ticker = ticker
        chart.draw(myCanvas)
        refreshLines(fibLines)
        refreshLines(riscLevels)
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

    private BooleanProperty _fibonacci1272extProperty= new SimpleBooleanProperty(true)
    @Override
    BooleanProperty fibonacci1272extProperty() {
        return _fibonacci1272extProperty
    }

    private Logger log = Logger.getLogger(getClass().getPackage().getName())
    private Map<Stock,List<CanvasGroup>> fibLines = new HashMap<>()
    private Map<Stock,List<CanvasGroup>> riscLevels = new HashMap<>()
    private Map<Stock,List<CanvasGroup>> levels = new HashMap<>()
}