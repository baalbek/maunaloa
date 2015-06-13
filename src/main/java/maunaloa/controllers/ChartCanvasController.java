package maunaloa.controllers;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.controllers.helpers.*;
import maunaloa.entities.windowdressing.LevelEntity;
import maunaloa.service.FxUtils;
import maunaloa.views.charts.ChartItem;
import oahu.domain.Tuple;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahu.financial.repository.StockMarketRepository;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;
import oahux.financial.DerivativeFx;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by rcs on 4/13/14.
 */
public class ChartCanvasController implements MaunaloaChartViewModel, DerivativesControllerListener {
    //region FXML
    @FXML private Canvas myCanvas;
    @FXML private VBox myContainer;
    @FXML private Pane myPane;
    //endregion FXML

    //region Init
    private FibonacciHelper fibonacciHelper;
    private LevelHelper levelHelper;
    private RiscLinesHelper riscLinesHelper;
    private SpotHelper spotHelper;

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
        riscLinesHelper = new RiscLinesHelper(this);
        spotHelper = new SpotHelper(this);
    }

    //endregion Init

    //region Private Methods
    //endregion Private Methods

    //region Events

    public void onNewFibonacciLine() {
        fibonacciHelper.onNewFibonacciLine();
    }
    public void onNewLevel() {
        levelHelper.onNewLevel();
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
                        vruler,
                        (levelEnt,newComment,wasFirstComment) -> {
                            System.out.println("NEW COMMENT: " + newComment.getCommentDate() + " " + newComment.getComment());
                            levelHelper.showComments(levelEnt);
                        });
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
        Consumer<AbstractControllerHelper> myUpdate = helper -> {
            Optional<List<ChartItem>> items = helper.items();
            if (items.isPresent()) {
                items.get().forEach(hub.getWindowDressingRepository()::saveOrUpdate);
            }
        };
        myUpdate.accept(levelHelper);
        myUpdate.accept(fibonacciHelper);
    }
    public void onSaveSelectedToRepos() {
        Consumer<AbstractControllerHelper> myUpdate = helper -> {
            Optional<List<ChartItem>> items = helper.items();
            if (items.isPresent()) {
                items.get().forEach(l -> {
                    MaunaloaStatus stat = l.getStatus();
                    if (stat.getChartLineStatus() == StatusCodes.SELECTED) {
                        hub.getWindowDressingRepository().saveOrUpdate(l);
                    }
                });
            }
        };
        myUpdate.accept(levelHelper);
        myUpdate.accept(fibonacciHelper);
    }
    public void showComments() {
        levelHelper.showComments();
    }
    public void hideComments() {
        levelHelper.hideComments();
    }
    public void setInactiveSelected() {
        levelHelper.items().ifPresent(ix -> {
            ix.stream().forEach(l -> {
                if (l.getStatus().getChartLineStatus() == StatusCodes.SELECTED) {
                    l.setEntityStatus(StatusCodes.ENTITY_TO_BE_INACTIVE);
                }
            });
        });
        fibonacciHelper.items().ifPresent(ix -> {
            ix.stream().forEach(l -> {
                if (l.getStatus().getChartLineStatus() == StatusCodes.SELECTED) {
                    l.setEntityStatus(StatusCodes.ENTITY_TO_BE_INACTIVE);
                }
            });
        });
    }
    public void setInactiveAll() {
        levelHelper.items().ifPresent(ix -> {
            ix.stream().forEach(l -> {
                l.setEntityStatus(StatusCodes.ENTITY_TO_BE_INACTIVE);
            });
        });
        fibonacciHelper.items().ifPresent(ix -> {
            ix.stream().forEach(l -> {
                l.setEntityStatus(StatusCodes.ENTITY_TO_BE_INACTIVE);
            });
        });
    }
    public void shiftLeft() {
        System.out.println("Shift left <-- 2");
        chart.shiftWeeks(2,myCanvas);
    }
    public void shiftRight() {

    }
    public void shiftToEnd() {
        chart.shiftToEnd(myCanvas);
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
    private LocalDate chartStartDate;

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
            riscLinesHelper.notifyStockChanging();
            spotHelper.notifyStockChanging();
            this.stock = stock;
            chart.draw(myCanvas);
            fibonacciHelper.notifyStockChanged();
            levelHelper.notifyStockChanged();
            riscLinesHelper.notifyStockChanged();
            spotHelper.notifyStockChanged();
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
        return hub.getStockRepository().findStockPrices(getStock().getTicker(),chartStartDate);
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

    //region DerivativesControllerListener
    @Override
    public void notifyDerivativesCalculated(List<DerivativeFx> calculated) {
        if (location > 4) return;

        riscLinesHelper.updateRiscs(calculated);
    }

    @Override
    public void notifySpotUpdated(StockPrice spot) {
        if ((location == 1) || (location == 3)) {
            spotHelper.updateSpot(spot);
        }
    }

    public void setChartStartDate(LocalDate chartStartDate) {
        this.chartStartDate = chartStartDate;
    }
    //endregion DerivativesControllerListener
}
