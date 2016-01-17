package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import maunaloa.charts.ChartItem;
import maunaloa.converters.TickerFileNamer;
import maunaloa.repository.ChartItemRepository;
import maunaloa.repository.DerivativeRepository;
import nz.sodium.StreamSink;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahu.financial.repository.StockMarketRepository;
import oahu.functional.Procedure4;
import oahux.chart.MaunaloaChart;
import oahux.controllers.ControllerCategory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by rcs on 4/12/14.
 *
 */
public class MainframeController {
    //legendPane.managedProperty().bind(legendPane.visibleProperty());
    //region FXML
    @FXML
    private ChartCanvasController candlesticksController;

    @FXML
    private ChartCanvasController weeksController;

    @FXML
    private DerivativesController optionsController;

    @FXML
    private ChoiceBox cbTickers;

    @FXML
    private ChoiceBox cbShiftAmount;

    @FXML
    private ToggleGroup rgDerivatives;

    @FXML
    private CheckBox cxLoadOptionsHtml;

    @FXML
    private CheckBox cxLoadStockHtml;

    @FXML
    private CheckBox cxComments;

    @FXML
    private CheckBox cxIsCloud;

    @FXML
    private TabPane myTabPane;

    @FXML
    private Label lblLocalMongodbUrl;

    @FXML
    private Label lblSqlUrl;

    @FXML
    private Button btnShiftLeft;

    @FXML
    private Button btnShiftRight;
    /*
    @FXML private CheckMenuItem mnuShiftAllCharts;
    @FXML private CheckMenuItem mnuIsShiftDays;
    */
    @FXML
    private BorderPane myBorderPane;

    //endregion FXML

    //region Initialize

    @SuppressWarnings("unchecked")
    public void initialize() {
        myBorderPane.setPrefSize(width,height);

        cbShiftAmount.getItems().add(1);
        cbShiftAmount.getItems().add(2);
        cbShiftAmount.getItems().add(3);
        cbShiftAmount.getItems().add(4);
        cbShiftAmount.getItems().add(5);
        cbShiftAmount.getItems().add(6);
        cbShiftAmount.getItems().add(7);
        cbShiftAmount.getItems().add(8);

        shiftAmountProperty.bind(cbShiftAmount.getSelectionModel().selectedItemProperty());

        /*
        shiftBothChartsProperty.bind(mnuShiftAllCharts.selectedProperty());

        isShiftDaysProperty.bind(mnuIsShiftDays.selectedProperty());
        */
        initControllers();
        initChoiceBoxTickers();
    }

    private void initControllers() {
        if (optionsController != null) {
            optionsController.selectedDerivativeProperty().bind(rgDerivatives.selectedToggleProperty());
            optionsController.selectedLoadStockProperty().bind(cxLoadStockHtml.selectedProperty());
            optionsController.selectedLoadDerivativesProperty().bind(cxLoadOptionsHtml.selectedProperty());
            optionsController.setMainframeController(this);
            optionsController.addStockChangedListener(stockCatAllCell);
        }
        Procedure4<ChartCanvasController,ControllerCategory,MaunaloaChart,nz.sodium.Cell<Stock>> initController =
                (controller,
                 location,
                 chart,
                 stockCell) -> {
                    controller.setControllerCategory(location);
                    controller.setChart(chart);
                    controller.setMainframeController(this);
                    controller.addStockChangedListener(stockCell);
                    _controllers.put(location, controller);
                    controller.addOptionRiscCalculatedListener(optionsController.riscCalculatedCell);
                };

        initController.apply(candlesticksController, ControllerCategory.DAY, candlesticksChart, stockCat1Cell);
        initController.apply(weeksController, ControllerCategory.WEEK, weeklyChart, stockCat1Cell);

    }

    @SuppressWarnings("unchecked")
    private void initChoiceBoxTickers() {
        final ObservableList<Stock> cbitems = FXCollections.observableArrayList(getStockRepository().getStocks());
        cbTickers.setConverter(new StringConverter<Stock>() {
            @Override
            public String toString(Stock o) {
                return String.format("[%s] %s",o.getTicker(),o.getCompanyName());
            }

            @Override
            public Stock fromString(String s) {
                throw new NotImplementedException();
            }
        });
        cbTickers.getItems().addAll(cbitems);
        /*
        Consumer<Stock> setStock = (Stock s) -> {
            System.out.println(String.format("[ %d ] %s, category: %d",s.getOid(), s.getCompanyName(), s.getTickerCategory()));
            switch (s.getTickerCategory()) {
                case 1:
                    candlesticksController.setStock(s);
                    optionsController.setStock(s);
                    weeksController.setStock(s);
                    break;
                case 2:
                    osebxCandlesticksController.setStock(s);
                    osebxWeeksController.setStock(s);
                    break;
                case 3:
                    //--->>> obxCandlesticksController.setStock(s);
                    //--->>> obxWeeksController.setStock(s);
                    break;
            }
        };
        */
        Consumer<Stock> setStock = (stock) -> {
            if (stock == null) {
                Object prop = cbTickers.valueProperty().get();
                if (prop == null) return;
                stock = (Stock)prop;
            }
            tickerFileNamer.setDownloadDate(candlesticksController.getLastCurrentDateShown());
            ((StreamSink)stockChanged).send(stock);
        };

        cbTickers.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observableValue, Number value, Number newValue) -> {
                    Stock s = cbitems.get(newValue.intValue());
                    setStock.accept(s);
                });

        cxLoadOptionsHtml.selectedProperty().addListener(e -> {
            if (cxLoadOptionsHtml.isSelected()) {
                setStock.accept(null);
            }
        });
    }

    //endregion Initialize

    //region ChartCanvasController

    private Map<ControllerCategory,ChartCanvasController> _controllers = new HashMap<>();

    private static ControllerCategory controllerEnumfromInt(int i) {
        switch (i) {
            case 2: return ControllerCategory.DAY;
            case 3: return ControllerCategory.WEEK;
            case 4: return ControllerCategory.OSEBX_DAY;
            case 5: return ControllerCategory.OSEBX_WEEK;
            default: return ControllerCategory.EMPTY;
        }
    }
    private Optional<ChartCanvasController> currentController() {
        int index =  myTabPane.getSelectionModel().getSelectedIndex();
        ControllerCategory ce = controllerEnumfromInt(index);
        return _controllers.containsKey(ce) ? Optional.of(_controllers.get(ce)) : Optional.empty();
    }
    //endregion ChartCanvasController

    //region Events
    private final nz.sodium.Stream<Stock> stockChanged = new StreamSink<>();
    private final nz.sodium.Cell<Stock> stockCat1Cell = stockChanged.filter(x -> x.getTickerCategory() == 1).hold(null);
    private final nz.sodium.Cell<Stock> stockCat2Cell = stockChanged.filter(x -> x.getTickerCategory() == 2).hold(null);
    private final nz.sodium.Cell<Stock> stockCatAllCell = stockChanged.hold(null);
    //endregion Events

    //region Properties
    private IntegerProperty shiftAmountProperty = new SimpleIntegerProperty(6);
    private BooleanProperty shiftBothChartsProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isShiftDaysProperty = new SimpleBooleanProperty(true);
    private TickerFileNamer tickerFileNamer;
    private double width = 1400.0;
    private double height = 850.0;
    private LocalDate chartStartDate;

    private ChartItemRepository chartItemRepository;
    private DerivativeRepository derivativeRepository;
    private StockMarketRepository stockRepository;
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;

    public void setChartStartDate(LocalDate chartStartDate) {
        this.chartStartDate = chartStartDate;
    }
    public LocalDate getChartStartDate() {
        return chartStartDate;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public DerivativeRepository getDerivativeRepository() {
        return derivativeRepository;
    }
    public void setDerivativeRepository(DerivativeRepository derivativeRepository) {
        this.derivativeRepository = derivativeRepository;
    }

    public void setStockRepository(StockMarketRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    public StockMarketRepository getStockRepository() {
        return stockRepository;
    }

    public void setTickerFileNamer(TickerFileNamer tickerFileNamer) {
        this.tickerFileNamer = tickerFileNamer;
    }

    public void setCandlesticksChart(MaunaloaChart candlesticksChart) {
        this.candlesticksChart = candlesticksChart;
    }

    public void setWeeklyChart(MaunaloaChart weeklyChart) {
        this.weeklyChart = weeklyChart;
    }

    public void setChartItemRepository(ChartItemRepository chartItemRepository) {
        this.chartItemRepository = chartItemRepository;
    }
    public ChartItemRepository getChartItemRepository() {
        return chartItemRepository;
    }


    //endregion Properties
}

