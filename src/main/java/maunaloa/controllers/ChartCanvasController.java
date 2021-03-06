package maunaloa.controllers;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import maunaloa.charts.OptionPriceSlider;
import maunaloa.charts.RiscLines;
import maunaloa.repository.ChartItemRepository;
import maunaloa.repository.ChartItemType;
import maunaloa.service.FxUtils;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.ControllerLocation;
import oahux.controllers.MaunaloaChartViewModel;
import oahux.financial.DerivativeFx;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by rcs on 30.11.15.
 *
 */
public class ChartCanvasController implements MaunaloaChartViewModel {
    //region FXML
    @FXML
    private Canvas myCanvas;

    @FXML
    private VBox myContainer;

    @FXML
    private Pane myPane;

    @FXML
    private TextField chartDate;
    //endregion FXML

    //region Shift
    public enum ShiftDirection { SHIFT_LEFT, SHIFT_RIGHT };
    public void shift(boolean isShiftDays, int amount, ShiftDirection shiftDirection) {
        if (shiftDirection == ShiftDirection.SHIFT_RIGHT) {
            amount = -amount;
        }
        if (isShiftDays) {
            chart.shiftDays(amount,myCanvas);
        }
        else {
            chart.shiftWeeks(amount,myCanvas);
        }
        //notifyChartShiftb();
    }
    public void shiftToEnd() {
        chart.shiftToEnd(myCanvas);
    }
    public void shiftToDate() {
        FxUtils.loadApp("/ShiftToDateCanvas.fxml", "Shift to date",
                new ShiftToDateController(shiftDate -> {
                    chart.shiftToDate(shiftDate,myCanvas);
                }));
    }
    public void shiftToDate(LocalDate shiftDate) {
        chart.shiftToDate(shiftDate, myCanvas);
    }
    //endregion Shift

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

    //region Events
    public void addStockChangedListener(nz.sodium.Cell<Stock> cell) {
        cell.listen( s -> {
            if (s == null) {
                return;
            }
            stock = s;
            chart.draw(myCanvas);
        });
    }
    public void addOptionSlidersShowListener(nz.sodium.Cell<List<DerivativeFx>> cell) {
        cell.listen(x -> {
            if (x == null) {
                return;
            }
            ChartItemRepository repos = mainframeController.getChartItemRepository();
            repos.removeLines(this,stock, ChartItemType.OPTION_PRICE_SLIDERS);
            List<OptionPriceSlider> sliders = x.stream().
                    map(fx -> new OptionPriceSlider(fx, vruler, Optional.of(repos))).
                    collect(Collectors.toList());
            repos.addOptionPriceSliders(this,stock,sliders);
        });
    }
    public void addOptionRiscCalculatedListener(nz.sodium.Cell<List<DerivativeFx>> cell) {
        cell.listen(x -> {
            if (x == null) {
                return;
            }
            ChartItemRepository repos = mainframeController.getChartItemRepository();
            List<RiscLines> riscs = x.stream().map(fx -> new RiscLines(fx, vruler)).collect(Collectors.toList());
            repos.removeLines(this,stock, ChartItemType.RISC_LINES);
            repos.addRiscLines(this,stock,riscs);
        });
    }
    public void onNewLevel() {
        ChartItemRepository repos = mainframeController.getChartItemRepository();

        if (stock != null) {
            myPane.setOnMouseReleased(e -> {

                double value = e.getY(); //vruler.calcValue(e.getY());
                repos.newLevelEntity(this, stock, vruler, value);
                //LevelLine line = new LevelLine(v:walue,vruler,col)
                /*
                LevelEntity entity =
                        new LevelEntity(stock.getTicker(),
                                boss.getLocation(),
                                value,
                                boss.getVruler());
                addNewLevel(entity);
                */
                myPane.setOnMouseReleased(null);
            });
        }
    }
    //endregion Events



    //region Properties
    private ControllerLocation controllerLocation;
    private MainframeController mainframeController;
    private MaunaloaChart chart;
    private Stock stock;
    private IRuler<Double> vruler;
    private IRuler<LocalDate> hruler;

    public Pane getPane() {
        return myPane;
    }
    public void setMainframeController(MainframeController mainframeController) {
        this.mainframeController = mainframeController;
    }

    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
        this.chart.setViewModel(this);
    }

    public ControllerLocation getControllerLocation() {
        return controllerLocation;
    }
    public void setControllerLocation(ControllerLocation controllerLocation) {
        this.controllerLocation = controllerLocation;
    }
    LocalDate getLastCurrentDateShown() {
        return chart.getLastCurrentDateShown(); //LocalDate.of(2015,2,16); //chart.getLastCurrentDateShown();
    }
    //endregion Properties

    //region MaunaloaChartViewModel
    @Override
    public Collection<StockPrice> stockPrices(int period) {
        return mainframeController.getStockRepository().findStockPrices(
                stock.getTicker(),
                mainframeController.getChartStartDate());
    }

    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public void setVruler(IRuler<Double> ruler) {
        this.vruler = ruler;
    }

    @Override
    public void setHruler(IRuler<LocalDate> ruler) {
        this.hruler = ruler;
    }

    //endregion MaunaloaChartViewModel

    //region Shift

    //endregion Shift
}
