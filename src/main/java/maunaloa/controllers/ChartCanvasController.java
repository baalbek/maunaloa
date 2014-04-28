package maunaloa.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import maunaloa.entities.windowdressing.FibLine;
import maunaloa.repository.StockRepository;
import maunaloa.repository.WindowDressingRepository;
import maunaloa.views.charts.ChartItem;
import oahu.domain.Tuple;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rcs on 4/13/14.
 */
public class ChartCanvasController implements MaunaloaChartViewModel {
    //region FXML
    @FXML private Canvas myCanvas;
    @FXML private VBox myContainer;
    @FXML private Pane myPane;
    private WindowDressingRepository windowDressingRepository;
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

    //region Private Methods
    private Tuple<IRuler> _rulers;
    private Tuple<IRuler> getRulers() {
        if (_rulers == null) {
            _rulers = new Tuple<>(getHruler(),getVruler());
        }
        return _rulers;
    }
    //endregion Private Methods

    //region Events
    ObjectProperty<Line> lineA = new SimpleObjectProperty<>();
    public void onNewFibonacciLine() {
        if (stock == null) {
            return;
        }
        myPane.setOnMousePressed(e -> {
            double x = getHruler().snapTo(e.getX());
            double y = e.getY();
            Line line = new Line(x, y, x, y);
            myPane.getChildren().add(line);
            lineA.set(line);
        });
        myPane.setOnMouseDragged(e -> {
            Line line = lineA.get();
            if (line != null) {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
            }
        });
        myPane.setOnMouseReleased(e -> {
            Line line = lineA.get();
            if (line != null) {
                myPane.getChildren().remove(line);

                line.setStartX(getHruler().snapTo(line.getStartX()));
                line.setEndX(getHruler().snapTo(line.getEndX()));

                FibLine fibline = new FibLine(stock.getTicker(),location,line,getRulers());

                myPane.getChildren().add(fibline.view());
            }
            lineA.set(null);
            myPane.setOnMousePressed(null);
            myPane.setOnMouseDragged(null);
            myPane.setOnMouseReleased(null);
        });
    }
    public void onFibLinesFromRepos() {
        List<ChartItem> items = windowDressingRepository.fetchFibLines(stock.getTicker(),location,0,getRulers());
        items.stream().forEach(item -> {
            System.out.println(item.view());
        });
    }
    public void addFibLines(List<ChartItem> items) {
        myPane.getChildren().addAll(items.stream().map(ChartItem::view).collect(Collectors.toList()));
    }
    //endregion Events

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

    public void setWindowDressingRepository(WindowDressingRepository windowDressingRepository) {
        this.windowDressingRepository = windowDressingRepository;
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
