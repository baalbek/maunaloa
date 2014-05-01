package maunaloa.controllers;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import maunaloa.controllers.helpers.FibonacciHelper;
import maunaloa.repository.StockRepository;
import maunaloa.views.charts.ChartItem;
import oahu.domain.Tuple;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;

import java.util.Collection;
import java.util.List;

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
    private FibonacciHelper fibonacciHelper;
    public void initialize() {
        InvalidationListener listener = e -> {
            if (stock == null) return;
            chart.draw(myCanvas);
        };
        myCanvas.widthProperty().bind(myContainer.widthProperty());
        myCanvas.heightProperty().bind(myContainer.heightProperty());

        myCanvas.widthProperty().addListener(listener);
        myCanvas.heightProperty().addListener(listener);
        fibonacciHelper = new FibonacciHelper(this);
    }

    //endregion Init

    //region Private Methods
    //endregion Private Methods

    //region Events

    public void onNewFibonacciLine() {
        fibonacciHelper.onNewFibonacciLine();
    }
    public void onFibLinesFromRepos() {
        List<ChartItem> items = hub.getWindowDressingRepository().fetchFibLines(stock.getTicker(), location, 0, getRulers());
        fibonacciHelper.onFibLinesFromRepos(items);
    }
    public void onDeleteSelLines() {
        fibonacciHelper.onDeleteSelLines();
    }
    public void onDeleteAllLines() {
        fibonacciHelper.onDeleteAllLines();

    }
    //endregion Events

    //region Properties
    private MaunaloaChart chart;
    private String name;
    private int location;
    private Stock stock;
    private IRuler vruler;
    private IRuler hruler;
    private ControllerHub hub;

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
        if (stock != null) {
            fibonacciHelper.notifyStockChanging();
            this.stock = stock;
            chart.draw(myCanvas);
            fibonacciHelper.notifyStockChanged();
        }
    }
    public ControllerHub getHub() {
        return hub;
    }

    public void setHub(ControllerHub hub) {
        this.hub = hub;
    }

    //endregion Properties

    //region MaunaloaChartViewModel
    @Override
    public Collection<StockPrice> stockPrices(int period) {
        return hub.getStockRepository().stockPrices(getStock().getTicker(),-1);
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

    @Override
    public Pane getPane() {
        return myPane;
    }

    @Override
    public int getLocation() {
        return location;
    }

    private Tuple<IRuler> _rulers;
    @Override
    public Tuple<IRuler> getRulers() {
        if (_rulers == null) {
            _rulers = new Tuple<>(getHruler(),getVruler());
        }
        return _rulers;
    }

    //endregion MaunaloaChartViewModel
}
