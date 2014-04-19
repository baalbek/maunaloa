package maunaloa.controllers;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import maunaloa.repository.StockRepository;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;

import java.util.Collection;

/**
 * Created by rcs on 4/13/14.
 */
public class ChartCanvasController implements MaunaloaChartViewModel {
    //region FXML
    @FXML private Canvas myCanvas;
    @FXML private VBox myContainer;
    @FXML private Pane myPane;
    //endregion FXML

    //region Init
    public void initialize() {
        InvalidationListener listener = e -> {
            if (stock == null) return;
            chart.draw(myCanvas);
        };
        myCanvas.widthProperty().bind(myContainer.widthProperty());
        myCanvas.heightProperty().bind(myContainer.heightProperty());

        myCanvas.widthProperty().addListener(listener);
        myCanvas.heightProperty().addListener(listener);
    }

    //endregion Init

    //region Properties
    private MaunaloaChart chart;
    private String name;
    private int location;
    private Stock stock;
    private IRuler vruler;
    private IRuler hruler;

    private StockRepository stockRepository;
    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(int location) {
        this.location = location;

    }
    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
        chart.setViewModel(this);
    }
    public void setStock(Stock stock) {
        this.stock = stock;
        if (stock != null) {
            chart.draw(myCanvas);
        }
    }
    public StockRepository getStockRepository() {
        return stockRepository;
    }

    public void setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    //endregion Properties


    //region MaunaloaChartViewModel
    @Override
    public Collection<StockPrice> stockPrices(int period) {
        return getStockRepository().stockPrices(getStock().getTicker(),-1);
    }

    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public IRuler getVruler() {
        return vruler;
    }

    @Override
    public void setVruler(IRuler ruler) {
        this.vruler = ruler;
    }

    @Override
    public IRuler getHruler() {
        return hruler;
    }

    @Override
    public void setHruler(IRuler ruler) {
        this.hruler = ruler;
    }

    //endregion MaunaloaChartViewModel
}
