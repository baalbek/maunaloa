package maunaloa.controllers.impl;

import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.domain.MongoDBResult;
import maunaloa.events.*;
import maunaloa.views.CanvasGroup;
import maunaloa.views.FibonacciDraggableLine;
import maunaloa.views.MongodbLine;
import maunaloa.views.RiscLines;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.domain.DerivativeFx;
import maunaloa.models.MaunaloaFacade;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/26/13
 * Time: 12:26 AM
 */
public class DefaultChartCanvasController implements ChartCanvasController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    @FXML private Canvas myCanvas;
    @FXML private VBox myContainer;
    @FXML private Pane myPane;

    private MaunaloaChart chart;
    private MaunaloaFacade model;
    private Stock ticker;
    private String name;

    Map<Stock,List<CanvasGroup>> fibLines = new HashMap<>();
    Map<Stock,List<CanvasGroup>> levels = new HashMap<>();


    final ObjectProperty<Line> lineA = new SimpleObjectProperty<>();
    private IRuler vruler;
    private IRuler hruler;
    private int location;

    //region Initialization Methods
    public void initialize() {
        initMyCanvas();
    }

    private void initMyCanvas() {
        InvalidationListener listener =     new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                if (ticker == null) return;
                chart.draw(myCanvas);
            }
        };

        myCanvas.widthProperty().bind(myContainer.widthProperty());
        myCanvas.heightProperty().bind(myContainer.heightProperty());

        myCanvas.widthProperty().addListener(listener);
        myCanvas.heightProperty().addListener(listener);
    }



    //endregion

    //region FXML Actions

    //endregion

    //region Public Methods

    public void draw() {
        chart.draw(myCanvas);
    }

    //endregion

    //region Fibonacci
    private void activateFibonacci() {
        myPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = getHRuler().snapTo(event.getX());
                double y = event.getY();
                Line line = new Line(x, y, x, y);
                myPane.getChildren().add(line);
                lineA.set(line);
            }
        });
        myPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Line line = lineA.get();
                if (line != null) {
                    line.setEndX(event.getX());
                    line.setEndY(event.getY());
                }
            }
        });
        myPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Line line = lineA.get();
                if (line != null) {
                    myPane.getChildren().remove(line);
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("Has Fibonacci extension: %s", fibonacci1272extProperty().get()));
                    }
                    line.setStartX(getHRuler().snapTo(line.getStartX()));
                    line.setEndX(getHRuler().snapTo(line.getEndX()));
                    final CanvasGroup fibLine = new FibonacciDraggableLine(line,
                                                                    getHRuler(),
                                                                    getVRuler(),
                                                                    fibonacci1272extProperty().get());
                    updateMyPaneLines(fibLine);
                }
                lineA.set(null);
                deactivateFibonacci();
            }
        });
    }
    private void deactivateFibonacci() {
        myPane.setOnMousePressed(null);
        myPane.setOnMouseDragged(null);
        myPane.setOnMouseReleased(null);
    }

    private void deleteLines(Map<Stock,List<CanvasGroup>> linesMap, boolean deleteAll) {
        List<CanvasGroup> lines = linesMap.get(getTicker());

        if (lines == null) return;

        List<CanvasGroup> linesToBeRemoved = new ArrayList<>();

        for (CanvasGroup l : lines) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Will attempt to delete line %s with status %d", l, l.getStatus()));
            }
            if ((deleteAll == true) || (l.getStatus() == CanvasGroup.SELECTED)) {
                myPane.getChildren().remove(l.view());
                linesToBeRemoved.add(l);
            }
        }

        for (CanvasGroup l : linesToBeRemoved) {
            lines.remove(l);
        }

        //lines.clear();
    }

    private void clearLines(Map<Stock,List<CanvasGroup>> linesMap) {
        List<CanvasGroup> lines = linesMap.get(getTicker());

        if (lines == null) return;

        for (CanvasGroup l : lines) {
            myPane.getChildren().remove(l.view());
        }
    }

    private void refreshLines(Map<Stock,List<CanvasGroup>> linesMap) {
        List<CanvasGroup> lines = linesMap.get(getTicker());

        if (lines == null) return;

        for (CanvasGroup l : lines) {
            myPane.getChildren().add(l.view());
        }
    }

    private void updateMyPaneLines(CanvasGroup line) {
        List<CanvasGroup> lines = fibLines.get(getTicker());
        if (lines == null) {
            lines = new ArrayList<>();
            fibLines.put(getTicker(), lines);
        }
        lines.add(line);
        myPane.getChildren().add(line.view());
    }

    //endregion Fibonacci

    //region Interface methods


    @Override
    public void setTicker(Stock ticker) {
        clearLines(fibLines);
        clearLines(levels);
        this.ticker = ticker;
        draw();
        refreshLines(fibLines);
        refreshLines(levels);
    }


    @Override
    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
        this.chart.setViewModel(this);
    }

    @Override
    public void setModel(MaunaloaFacade model) {
        this.model = model;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLocation(int loc) {
        location = loc;
    }

    private BooleanProperty _fibonacci1272extProperty= new SimpleBooleanProperty(true);
    @Override
    public BooleanProperty fibonacci1272extProperty() {
        return _fibonacci1272extProperty;
    }

    @Override
    public Collection<StockPrice> stockPrices(int i) {
        return model.stockPrices(getTicker().getTicker(),-1);
    }

    @Override
    public Stock getTicker() {
        return ticker;
    }

    @Override
    public IRuler getVRuler() {
        return vruler;
    }

    @Override
    public void setVRuler(IRuler ruler) {
        this.vruler = ruler;
    }

    @Override
    public IRuler getHRuler() {
        return hruler;
    }

    @Override
    public void setHRuler(IRuler ruler) {
        this.hruler = ruler;
    }
    //endregion  Interface methods

    //region  DerivativesControllerListener Interface methods
    @Override
    public void notify(DerivativesCalculatedEvent event) {
        deleteLines(levels,true);
        List<CanvasGroup> lines = new ArrayList<>();
        for(DerivativeFx d : event.getCalculated()) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("notify adding line for derivative %s",d.getTicker()));
            }
            lines.add(new RiscLines(d, vruler));
        }
        levels.put(getTicker(),lines);
        refreshLines(levels);
    }

    @Override
    public void notify(StockPriceAssignedEvent event) {
        StockPrice sp = event.getStockPrice();
        System.out.println("Evenjt fired: " + event + ", " + sp);
    }
    //endregion  DerivativesControllerListener Interface methods

    //region  MongoDBControllerListener Interface methods
    @Override
    public void onMongoDBEvent(MongoDBEvent event) {
        if (event.getLocation() != this.location) return;

        switch (event.getAction()) {
            case MongoDBEvent.SAVE_TO_DATASTORE:
                List<CanvasGroup> lines = fibLines.get(getTicker());
                for (CanvasGroup line : lines) {
                    if (line.getStatus() == CanvasGroup.SELECTED) {
                        MongodbLine mongoLine = (MongodbLine)line;
                        DBObject p1 = mongoLine.coord(MongodbLine.P1);
                        DBObject p2 = mongoLine.coord(MongodbLine.P2);
                        String tix = getTicker().getTicker();
                        MongoDBResult result = model.getWindowDressingModel().saveFibonacci(tix,location,p1,p2);
                        if (result.isOk()) {
                            log.info(String.format("(%s) Successfully saved fibline with _id: %s to location: %d",
                                    tix,
                                    result.getObjectId(),
                                    location));
                            mongoLine.setMongodbId(result.getObjectId());
                            line.setStatus(CanvasGroup.SAVED_TO_DB);
                        }
                        else {
                            log.error(String.format("(Saving fibline %s, %d) %s",tix,location,result.getWriteResult().getError()));
                        }
                    }
                    else if (line.getStatus() == CanvasGroup.SAVED_TO_DB_SELECTED) {
                        MongodbLine mongoLine = (MongodbLine)line;
                        DBObject p1 = mongoLine.coord(MongodbLine.P1);
                        DBObject p2 = mongoLine.coord(MongodbLine.P2);
                        String tix = getTicker().getTicker();

                        WriteResult result = model.getWindowDressingModel().updateCoord(mongoLine.getMongodbId(),p1,p2);
                        if (result.getLastError().ok()) {
                            log.info(String.format("(%s) Successfully updated fibline with _id: %s to location: %d",
                                    tix,
                                    mongoLine.getMongodbId(),
                                    location));
                            line.setStatus(CanvasGroup.SAVED_TO_DB);
                        }
                        else {
                            log.error(String.format("(Updating fibline %s, %d) %s",tix,location,result.getError()));
                        }
                    }
                }
                break;
            case MongoDBEvent.FETCH_FROM_DATASTORE:
                for (DBObject o : event.getLines()) {
                    MongodbLine line = createLineFromDBObject(o);


                }
                break;
        }
    }

    private MongodbLine createLineFromDBObject(DBObject obj) {
        DBObject p1 = (DBObject)obj.get("p1");
        DBObject p2 = (DBObject)obj.get("p2");


        double p2x = hruler.calcPix(p2.get("x"));
        double p2y = vruler.calcPix(p2.get("y"));

        System.out.println("p2x: " + p2x + ", p2y: " + p2y);

        /*
        final CanvasGroup fibLine = new FibonacciDraggableLine(
                getHRuler(),
                getVRuler(),
                fibonacci1272extProperty().get());
        */
        return null;
    }
    //endregion MongoDBControllerListener  Interface methods

    //region  MainFrameControllerListener Interface methods
    @Override
    public void onFibonacciEvent(FibonacciEvent event) {
        if (event.getLocation() != this.location) return;

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
    //endregion  MainFrameControllerListener Interface methods

}
