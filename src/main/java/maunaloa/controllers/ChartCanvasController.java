package maunaloa.controllers;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import maunaloa.MaunaloaStatus;
import maunaloa.controllers.helpers.FibonacciHelper;
import maunaloa.controllers.helpers.LevelHelper;
import maunaloa.entities.windowdressing.LevelEntity;
import maunaloa.repository.StockRepository;
import maunaloa.service.FxUtils;
import maunaloa.views.charts.ChartItem;
import oahu.domain.Tuple;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;
import org.bson.types.ObjectId;

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
    private LevelHelper levelHelper;
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
        levelHelper = new LevelHelper(this);
    }

    //endregion Init

    //region Private Methods
    //endregion Private Methods

    //region Events

    public void onNewFibonacciLine() {
        fibonacciHelper.onNewFibonacciLine();
    }
    public void onNewLevel() {
        NewLevelController controller = new NewLevelController((v) -> {
            Stock stock = getStock();
            if (stock != null) {
                LevelEntity entity = new LevelEntity(stock.getTicker(),location,v,getVruler());
                levelHelper.addNewLevel(entity);
            }
        });
        FxUtils.loadApp("/NewLevelDialog.fxml", "New Level", controller);
    }
    public void onFibLinesFromRepos() {
        List<ChartItem> items =
                hub.getWindowDressingRepository().fetchFibLines(
                        stock.getTicker(),
                        location,
                        0,
                        getRulers());
        fibonacciHelper.onLinesFromRepos(items);
    }
    public void onLevelsFromRepos() {
        List<ChartItem> items =
                hub.getWindowDressingRepository().fetchLevels(
                        stock.getTicker(),
                        location,
                        0,
                        vruler);
        levelHelper.onLinesFromRepos(items);
    }
    public void onDeleteSelLines() {
        fibonacciHelper.onDeleteSelLines();
        levelHelper.onDeleteSelLines();
    }
    public void onDeleteAllLines() {
        fibonacciHelper.onDeleteAllLines();
        levelHelper.onDeleteAllLines();
    }
    public void onSaveAllToRepos() {
        List<ChartItem> levels = levelHelper.items();
        levels.forEach(l -> {
            MaunaloaStatus stat = l.getStatus();
            System.out.println(String.format("Ent. stat: %d, line stat: %d",
                    stat.getEntityStatus(),
                    stat.getChartLineStatus()));
        });
    }
    public void onSaveSelectedToRepos() {
        List<ChartItem> levels = levelHelper.items();
        levels.forEach(l -> {
            MaunaloaStatus stat = l.getStatus();
            System.out.println(String.format("Ent. stat: %d, line stat: %d",
                                              stat.getEntityStatus(),
                                              stat.getChartLineStatus()));
        });
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
            levelHelper.notifyStockChanging();
            this.stock = stock;
            chart.draw(myCanvas);
            fibonacciHelper.notifyStockChanged();
            levelHelper.notifyStockChanged();
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
