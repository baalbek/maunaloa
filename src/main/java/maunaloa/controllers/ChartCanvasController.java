package maunaloa.controllers;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.ControllerCategory;
import oahux.controllers.MaunaloaChartViewModel;
import oahux.financial.DerivativeFx;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

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
    public void addOptionRiscCalculatedListener(nz.sodium.Cell<List<DerivativeFx>> cell) {
        cell.listen(x -> {
            if (x == null) {
                return;
            }
        });
    }
    //endregion Events



    //region Properties
    private ControllerCategory controllerCategory;
    private MainframeController mainframeController;
    private MaunaloaChart chart;
    private Stock stock;
    private IRuler vruler;
    private IRuler hruler;

    public void setMainframeController(MainframeController mainframeController) {
        this.mainframeController = mainframeController;
    }

    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
        this.chart.setViewModel(this);
    }

    public void setControllerCategory(ControllerCategory controllerCategory) {
        this.controllerCategory = controllerCategory;
    }
    LocalDate getLastCurrentDateShown() {
        return LocalDate.of(2015,2,16); //chart.getLastCurrentDateShown();
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
        this.vruler = ruler;
    }
    //endregion MaunaloaChartViewModel

    //region Shift

    //endregion Shift
}
