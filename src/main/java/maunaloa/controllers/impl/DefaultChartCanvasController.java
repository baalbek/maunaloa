package maunaloa.controllers.impl;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import maunaloa.controllers.ChartCanvasController;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.models.MaunaloaFacade;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/26/13
 * Time: 12:26 AM
 */
public class DefaultChartCanvasController implements ChartCanvasController {
    @FXML private Canvas myCanvas;
    @FXML private VBox myContainer;

    private MaunaloaChart chart;
    private MaunaloaFacade model;
    private String ticker;

    //region Initialization Methods

    private void initMyCanvas() {
        InvalidationListener listener =     new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                //if (ticker == null) return;
                //chart.draw(myCanvas);
            }
        };

        //*
        myCanvas.widthProperty().bind(myContainer.widthProperty());
        myCanvas.heightProperty().bind(myContainer.heightProperty());
        //*/

        myCanvas.widthProperty().addListener(listener);
        myCanvas.heightProperty().addListener(listener);
    }



    //endregion

    //region Private Methods

    public void draw() {

        /*
        System.out.println("myCanvas: " + myCanvas +
                            ", h: " + myCanvas.getHeight() +
                            ", w: " + myCanvas.getWidth() +
                            ", x: " + myCanvas.getTranslateX() +
                            ", y: " + myCanvas.getTranslateY());
        GraphicsContext ctx = myCanvas.getGraphicsContext2D();

        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.lineTo(1200, 1200);
        ctx.closePath();
        ctx.setStroke(Color.BLACK);
        ctx.stroke();
        //*/
        chart.draw(myCanvas);
    }
    //endregion

    //region Fibonacci

    //endregion Fibonacci

    //region Interface methods

    @Override
    public void setTicker(String ticker) {
        this.ticker = ticker;
        draw();
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
    public Collection<StockPrice> stockPrices(int i) {
        return null;
    }

    @Override
    public String getTicker() {
        return null;
    }

    @Override
    public IRuler getRuler() {
        return null;
    }

    @Override
    public void setRuler(IRuler iRuler) {
    }

    //endregion  Interface methods
}
