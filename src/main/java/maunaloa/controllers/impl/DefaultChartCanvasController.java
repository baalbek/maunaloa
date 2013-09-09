package maunaloa.controllers.impl;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.events.DerivativesCalculatedEvent;
import maunaloa.views.CanvasLine;
import maunaloa.views.FibonacciDraggableLine;
import maunaloa.views.LevelLine;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.domain.DerivativeFx;
import oahux.models.MaunaloaFacade;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/26/13
 * Time: 12:26 AM
 */
public class DefaultChartCanvasController implements ChartCanvasController {
    @FXML private Canvas myCanvas;
    @FXML private VBox myContainer;
    @FXML private Pane myPane;

    private MaunaloaChart chart;
    private MaunaloaFacade model;
    private Stock ticker;
    private String name;

    Map<Stock,List<CanvasLine>> fibLines = new HashMap<>();
    Map<Stock,List<CanvasLine>> levels = new HashMap<>();


    final ObjectProperty<Line> lineA = new SimpleObjectProperty<>();
    private IRuler ruler;

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
                double x = event.getX();
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
                    CanvasLine fibLine = new FibonacciDraggableLine(line,getRuler());
                    updateMyPaneLines(fibLine);
                    myPane.getChildren().add(fibLine.view());
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

    private void deleteLines(Map<Stock,List<CanvasLine>> linesMap) {
        List<CanvasLine> lines = linesMap.get(getTicker());

        if (lines == null) return;

        for (CanvasLine l : lines) {
            myPane.getChildren().remove(l.view());
        }

        lines.clear();
    }
    private void clearLines(Map<Stock,List<CanvasLine>> linesMap) {
        List<CanvasLine> lines = linesMap.get(getTicker());

        if (lines == null) return;

        for (CanvasLine l : lines) {
            myPane.getChildren().remove(l.view());
        }
    }

    private void refreshLines(Map<Stock,List<CanvasLine>> linesMap) {
        List<CanvasLine> lines = linesMap.get(getTicker());

        if (lines == null) return;

        for (CanvasLine l : lines) {
            myPane.getChildren().add(l.view());
        }
    }

    private void updateMyPaneLines(CanvasLine line) {
        List<CanvasLine> lines = fibLines.get(getTicker());
        if (lines == null) {
            lines = new ArrayList<>();
            fibLines.put(getTicker(), lines);
        }
        lines.add(line);
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
    public void setMenuBar(MenuBar menuBar) {
        Menu menu = new Menu(String.format("%s",name));
        MenuItem m1 = new MenuItem("Fibonacci line");
        m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                activateFibonacci();
            }
        });
        /*
        MenuItem m2 = new MenuItem("Deactivate");
        m2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                deactivateFibonacci();
            }
        });
        */
        MenuItem m3 = new MenuItem("Delete fibonacci");
        m3.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                deleteLines(fibLines);
            }
        });
        menu.getItems().addAll(m1,m3, new SeparatorMenuItem());
        menuBar.getMenus().add(menu);
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public IRuler getRuler() {
        return ruler;
    }

    @Override
    public void setRuler(IRuler ruler) {
        this.ruler = ruler;
    }

    @Override
    public void notify(DerivativesCalculatedEvent event) {
        deleteLines(levels);
        List<CanvasLine> lines = new ArrayList<>();
        for(DerivativeFx d : event.getCalculated()) {
            lines.add(new LevelLine(d, ruler));
        }
        levels.put(getTicker(),lines);
        refreshLines(levels);
    }

    //endregion  Interface methods
}
