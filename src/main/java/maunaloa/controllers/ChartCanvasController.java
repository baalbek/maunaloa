package maunaloa.controllers;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.controllers.helpers.*;
import maunaloa.service.FxUtils;
import maunaloa.views.charts.ChartItem;
import oahu.dto.Tuple;
import oahu.dto.Tuple2;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.ControllerEnum;
import oahux.controllers.MaunaloaChartViewModel;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.LoggingPermission;

/**
 * Created by rcs on 4/13/14.
 *
 */
public class ChartCanvasController implements MaunaloaChartViewModel {
    //region FXML
    @FXML private Canvas myCanvas;
    @FXML private VBox myContainer;
    @FXML private Pane myPane;
    @FXML private TextField chartDate;

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
        fibonacciHelper = new FibonacciHelper(this);
        levelHelper = new LevelHelper(this);
        sliderHelper = new OptionPriceSliderHelper(this);
        riscLinesHelper = new RiscLinesHelper(this);
        spotHelper = new SpotHelper(this);
        dateLineHelper = new DateLineHelper(this);
    }

    //endregion Init

    //region Events

    public void onNewDateLine() {
        dateLineHelper.onNewDateLine();
    }
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
    public void shiftLeft(boolean isShiftDays, int amount) {
        if (isShiftDays == true) {
            chart.shiftDays(amount,myCanvas);
        }
        else {
            chart.shiftWeeks(amount,myCanvas);
        }
        notifyChartShift();
    }
    public void shiftRight(boolean isShiftDays, int amount) {
        if (isShiftDays == true) {
            chart.shiftDays(-amount,myCanvas);
        }
        else {
            chart.shiftWeeks(-amount,myCanvas);
        }
        notifyChartShift();
    }
    public void shiftToEnd() {
        chart.shiftToEnd(myCanvas);
        notifyChartShift();
    }

    public void shiftToDate() {
        FxUtils.loadApp("/ShiftToDateCanvas.fxml", "Shift to date",
                new ShiftToDateController(this::shiftToDate));
    }

    public void shiftToDate(LocalDate shiftDate) {
        chart.shiftToDate(shiftDate,myCanvas);
        notifyChartShift();
    }
    private void notifyChartShift() {
        Tuple2<IRuler<LocalDate>,IRuler<Double>> rulers = getRulers();
        levelHelper.updateRulers(rulers);
        sliderHelper.updateRuler(rulers.second());
        fibonacciHelper.updateRulers(rulers);
        spotHelper.updateRulers(rulers);
        riscLinesHelper.updateRuler(rulers.second());
        dateLineHelper.updateRulers(rulers);
        chartDate.setText(String.format("Chart date: %s",chart.getLastCurrentDateShown().toString()));
    }
    public void addNotifyDerivativesCalculated(DerivativesController controller) {
        controller.addOnCalculatedEvents((calculated) -> {
            switch (location) {
                case DAY:
                case WEEK:
                    riscLinesHelper.updateRiscs(calculated);
            }
        });
    }

    public void addNotifySpotUpdated(DerivativesController controller) {
        controller.addOnAssignSpotEvents((spot) -> {
            switch (location) {
                case DAY:
                case WEEK:
                    spotHelper.updateSpot(spot);
            }
        });
    }
    public void addNewOptionPriceSliderEvent(DerivativesController controller) {
        controller.addNewOptionPriceSliderEvents((selected) -> {
            switch (location) {
                case DAY:
                case WEEK:
                    sliderHelper.updateSliders(selected);

            }
        });
    }
    //endregion Events

    //region Properties
    private DateLineHelper dateLineHelper;
    private FibonacciHelper fibonacciHelper;
    private LevelHelper levelHelper;
    private OptionPriceSliderHelper sliderHelper; 
    private RiscLinesHelper riscLinesHelper;
    private SpotHelper spotHelper;
    private MaunaloaChart chart;
    private String name;
    private ControllerEnum location;
    private Stock stock;
    private IRuler<Double> vruler;
    private IRuler<LocalDate> hruler;
    private ControllerHub hub;
    private LocalDate chartStartDate;

    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(ControllerEnum location) {
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
            chartDate.setText(String.format("Chart date: %s",chart.getLastCurrentDateShown().toString()));
        }
    }
    public ControllerHub getHub() {
        return hub;
    }

    public void setHub(ControllerHub hub) {
        this.hub = hub;
    }

    LocalDate getLastCurrentDateShown() {
        return chart.getLastCurrentDateShown();
    }

    public void setChartStartDate(LocalDate chartStartDate) {
        this.chartStartDate = chartStartDate;
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
    public IRuler<Double> getVruler() {
        return vruler;
    }

    @Override
    public void setVruler(IRuler<Double> ruler) {
        this.vruler = ruler;
        _rulers = null;
    }

    @Override
    public IRuler<LocalDate> getHruler() {
        return hruler;
    }

    @Override
    public void setHruler(IRuler<LocalDate> ruler) {
        this.hruler = ruler;
        _rulers = null;
    }

    @Override
    public Pane getPane() {
        return myPane;
    }

    @Override
    public ControllerEnum getLocation() {
        return location;
    }

    private Tuple2<IRuler<LocalDate>,IRuler<Double>> _rulers;
    @Override
    public Tuple2<IRuler<LocalDate>,IRuler<Double>> getRulers() {
        if (_rulers == null) {
            _rulers = new Tuple2<>(getHruler(),getVruler());
        }
        return _rulers;
    }

    //endregion MaunaloaChartViewModel

}
