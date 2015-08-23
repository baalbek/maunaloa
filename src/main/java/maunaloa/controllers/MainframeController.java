package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import maunaloa.repository.DerivativeRepository;
import maunaloa.repository.WindowDressingRepository;
import maunaloa.service.FxUtils;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahu.financial.repository.StockMarketRepository;
import oahu.functional.Procedure0;
import oahu.functional.Procedure4;
import oahux.chart.MaunaloaChart;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by rcs on 4/12/14.
 *
 */
public class MainframeController implements ControllerHub {
    //region FXML
    @FXML private ChartCanvasController candlesticksController;
    @FXML private ChartCanvasController weeksController;
    @FXML private ChartCanvasController osebxCandlesticksController;
    @FXML private ChartCanvasController osebxWeeksController;
    /*
    @FXML private ChartCanvasController obxCandlesticksController;
    @FXML private ChartCanvasController obxWeeksController;
    */
    @FXML private DerivativesController optionsController;
    @FXML private ChoiceBox cbTickers;
    @FXML private ChoiceBox cbShiftAmount;
    @FXML private ToggleGroup rgDerivatives;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;
    @FXML private CheckBox cxComments;
    @FXML private CheckBox cxIsCloud;
    @FXML private TabPane myTabPane;
    @FXML private Label lblLocalMongodbUrl;
    @FXML private Label lblSqlUrl;
    @FXML private Button btnShiftLeft;
    @FXML private Button btnShiftRight;
    @FXML private CheckMenuItem mnuShiftAllCharts;
    @FXML private CheckMenuItem mnuIsShiftDays;


    static final int CONTROLLER_DAY = 2;
    static final int CONTROLLER_WEEK = 3;
    static final int CONTROLLER_OSEBX_DAY = 4;
    static final int CONTROLLER_OSEBX_WEEK = 5;
   /* @FXML private MenuBar myMenuBar;
    @FXML private Menu linesMenu;
    @FXML private Menu mongodbMenu;
    */

    //endregion FXML

    /*
    private enum Controller {
        DAY(2), WEEK(3), OSEBX_DAY(4), OSEBX_WEEK(5);
        private final int index;
        Controller(int index) {
            this.index = index;
        }
        public int getIndex() {
            return index;
        }
    }
    //*/

    //region Private Methods
    private Optional<ChartCanvasController> currentController() {
        int index =  myTabPane.getSelectionModel().getSelectedIndex();
        ChartCanvasController result = null;
        switch (index) {
            case CONTROLLER_DAY: result = candlesticksController;
                break;
            case CONTROLLER_WEEK: result = weeksController;
                break;
            case CONTROLLER_OSEBX_DAY: result = osebxCandlesticksController;
                break;
            case CONTROLLER_OSEBX_WEEK: result = osebxWeeksController;
                break;
            /* --->>>
            case 3: result = obxCandlesticksController;
                break;
            case 4: result = obxWeeksController;
                break;
            //*/
        }
        return result == null ? Optional.empty() : Optional.of(result);
    }
    //endregion Private Methods

    //region Events
    public void close(ActionEvent event)  {
        System.exit(0);
    }
    public void onNewFibonacciLine(ActionEvent event)  {
        currentController().ifPresent(ChartCanvasController::onNewFibonacciLine);
    }
    public void onNewLevel(ActionEvent event)  {
        currentController().ifPresent(ChartCanvasController::onNewLevel);
        /*
        currentController().ifPresent(c -> {
            System.out.println(c.getLastCurrentDateShown());
        });
        */
    }
    public void onFibLinesFromRepos(ActionEvent event) {
        //currentController().ifPresent(ChartCanvasController::onFibLinesFromRepos);
        currentController().ifPresent(c -> {
            c.onFibLinesFromRepos();
            if (cxComments.isSelected()) {
                showComments();
            }
        });
    }
    public void onLevelsFromRepos(ActionEvent event) {
        currentController().ifPresent(c -> {
            c.onLevelsFromRepos();
            if (cxComments.isSelected()) {
                showComments();
            }
        });
    }
    public void onDeleteSelLines(ActionEvent event) {
        currentController().ifPresent(ChartCanvasController::onDeleteSelLines);
    }
    public void onDeleteAllLines(ActionEvent event) {
        currentController().ifPresent(ChartCanvasController::onDeleteAllLines);
    }
    public void onSaveSelectedToRepos(ActionEvent event) {
        currentController().ifPresent(ChartCanvasController::onSaveSelectedToRepos);
    }
    public void onSaveAllToRepos(ActionEvent event) {
        currentController().ifPresent(ChartCanvasController::onSaveAllToRepos);
    }
    public void onSetInactiveSelLines(ActionEvent event) {
        currentController().ifPresent(ChartCanvasController::setInactiveSelected);
    }
    public void onSetInactiveAllLines(ActionEvent event) {
        currentController().ifPresent(ChartCanvasController::setInactiveAll);
    }
    public void onShowAllComments(ActionEvent event) {
        showComments();
    }
    public void onHideAllComments(ActionEvent event) {
        hideComments();
    }

    public void onUpdateDerivatives(ActionEvent event) {
        optionsController.updateDerivatives();
    }

    public void onUpdateSpot(ActionEvent event) {
        optionsController.updateSpot();
    }

    public void onLogin(ActionEvent event) {

    }
    public void onLogout(ActionEvent event) {

    }
    public void onInvalidateDerivativesRepository (ActionEvent event) {
        derivativeRepository.invalidate();
    }
    public void onShiftToEnd(ActionEvent event) {
        if (shiftBothChartsProperty.get() == true) {
            candlesticksController.shiftToEnd();
            weeksController.shiftToEnd();
        }
        else {
            currentController().ifPresent(ChartCanvasController::shiftToDate);
        }
    }
    public void onShiftToDate(ActionEvent event) {
        if (shiftBothChartsProperty.get() == true) {
            FxUtils.loadApp("/ShiftToDateCanvas.fxml", "Shift to date",
                    new ShiftToDateController(shiftDate -> {
                        candlesticksController.shiftToDate(shiftDate);
                        weeksController.shiftToDate(shiftDate);
                        System.out.println(String.format("Last date shown: %s", candlesticksChart.getLastCurrentDateShown()));
                    }));
        }
        else {
            Optional<ChartCanvasController>  ctrl = currentController();
            if (ctrl.isPresent() == true) {
                ctrl.get().shiftToDate();
            }
            else {
                candlesticksController.shiftToDate();
            }
            //currentController().ifPresent(ChartCanvasController::shiftToDate);
        }
    }
    //endregion Events

    //region Initialize

    @SuppressWarnings("unchecked")
    public void initialize() {
        initContextInfo();
        initNavButtons();
        initChoiceBoxTickers();
        LocalDate csd = LocalDate.of(2012,1,1);
        Procedure4<ChartCanvasController,String,Integer,MaunaloaChart> initController =
                (ChartCanvasController controller,
                                String name,
                                Integer location,
                                MaunaloaChart chart) -> {
                    controller.setName(name);
                    controller.setLocation(location);
                    controller.setChart(chart);
                    controller.setHub(this);
                    controller.setChartStartDate(csd);
                    System.out.println("Setting up controller " + name);
                };


        initController.apply(candlesticksController, "Candlesticks", 1, candlesticksChart);
        initController.apply(weeksController, "Weeks", 2, weeklyChart);
        initController.apply(osebxCandlesticksController, "OSEBX Candlest.", 5, osebxCandlesticksChart);
        initController.apply(osebxWeeksController, "OSEBX Weeks", 6, osebxWeeklyChart);
        /*--->>>
        initController.apply(obxCandlesticksController, "OBX Candlest.", 3, obxCandlesticksChart);
        initController.apply(obxWeeksController, "OBX Weeks", 4, obxWeeklyChart);
        */

        initOptionsController();

        cbShiftAmount.getItems().add(1);
        cbShiftAmount.getItems().add(2);
        cbShiftAmount.getItems().add(3);
        cbShiftAmount.getItems().add(4);
        cbShiftAmount.getItems().add(5);
        cbShiftAmount.getItems().add(6);
        cbShiftAmount.getItems().add(7);
        cbShiftAmount.getItems().add(8);

        shiftAmountProperty.bind(cbShiftAmount.getSelectionModel().selectedItemProperty());

        shiftBothChartsProperty.bind(mnuShiftAllCharts.selectedProperty());

        isShiftDaysProperty.bind(mnuIsShiftDays.selectedProperty());
    }

    private void initOptionsController() {

        Procedure0 setStock = () -> {
            Object prop = cbTickers.valueProperty().get();
            if (prop == null) return;
            Stock stock = (Stock)prop;
            System.out.println(stock.getTicker());
            if (listener != null) {
                listener.setDownloadDate(candlesticksChart.getLastCurrentDateShown());
            }
            optionsController.setStock(stock);
        };

        rgDerivatives.selectedToggleProperty().addListener(e -> setStock.apply());
        cxLoadOptionsHtml.selectedProperty().addListener(e -> {
            if (cxLoadOptionsHtml.isSelected()) {
                setStock.apply();
            }
        });
        cxLoadStockHtml.selectedProperty().addListener(e -> {
            if (cxLoadStockHtml.isSelected()) {
                setStock.apply();
            }
        });
        /*cxComments.selectedProperty().addListener(e -> {
            if (cxComments.isSelected()) {
                showComments();
            }
            else {
                hideComments();
            }
        });*/

        if (optionsController != null) {
            optionsController.selectedDerivativeProperty().bind(rgDerivatives.selectedToggleProperty());
            optionsController.selectedLoadStockProperty().bind(cxLoadStockHtml.selectedProperty());
            optionsController.selectedLoadDerivativesProperty().bind(cxLoadOptionsHtml.selectedProperty());
            optionsController.setDerivativeRepository(getDerivativeRepository());
            //*
            optionsController.addDerivativesControllerListener(candlesticksController);
            optionsController.addDerivativesControllerListener(weeksController);
            //*/
        }

        //--->>> optionsController.addDerivativesControllerListener(candlesticksController);
        //--->>> optionsController.addDerivativesControllerListener(weeksController);
        //--->>> optionsController.addDerivativesControllerListener(obxCandlesticksController);
    }
    private void initContextInfo() {
        cxIsCloud.setSelected(windowDressingRepository.isCloud());

        //lblLocalMongodbUrl.text = 'MongoDB:' + windowDressingModel.mongodbHost
        lblSqlUrl.setText("SQL: " + sqldbUrl);

        cxIsCloud.selectedProperty().addListener(event -> {
            windowDressingRepository.setCloud(cxIsCloud.isSelected());
        });
    }
    private void initNavButtons() {
        btnShiftLeft.setOnAction(e -> {
            if (shiftBothChartsProperty.get() == true) {
                candlesticksController.shiftLeft(isShiftDaysProperty.get(),shiftAmountProperty.get());
                weeksController.shiftLeft(isShiftDaysProperty.get(),shiftAmountProperty.get());
            }
            else {
                currentController().ifPresent(controller ->
                        controller.shiftLeft(isShiftDaysProperty.get(), shiftAmountProperty.get()));
            }
        });
        btnShiftRight.setOnAction(e -> {
            if (shiftBothChartsProperty.get() == true) {
                candlesticksController.shiftRight(isShiftDaysProperty.get(),shiftAmountProperty.get());
                weeksController.shiftRight(isShiftDaysProperty.get(),shiftAmountProperty.get());
            }
            else {
                currentController().ifPresent(controller ->
                        controller.shiftRight(isShiftDaysProperty.get(), shiftAmountProperty.get()));
            }
        });
    }
    @SuppressWarnings("unchecked")
    private void initChoiceBoxTickers() {
        final ObservableList<Stock> cbitems = FXCollections.observableArrayList(stockRepository.getStocks());
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
            /*
            Collection<StockPrice> prices = getStockRepository().stockPrices(s.getTicker(), 1);
            System.out.println(String.format("Num: %d", prices.size()));
            */
        };

        cbTickers.getSelectionModel().selectedIndexProperty().addListener(
            (ObservableValue<? extends Number> observableValue, Number value, Number newValue) -> {
                setStock.accept(cbitems.get(newValue.intValue()));
        });
    }
    private void showComments() {
        currentController().ifPresent(ChartCanvasController::showComments);
    }
    private void hideComments() {
        currentController().ifPresent(ChartCanvasController::hideComments);
    }
    //endregion Initialize

    //region Properties
    //private Stock currentStock;
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;
    private MaunaloaChart obxCandlesticksChart;
    private MaunaloaChart obxWeeklyChart;
    private MaunaloaChart osebxCandlesticksChart;
    private MaunaloaChart osebxWeeklyChart;
    private StockMarketRepository stockRepository;
    private DerivativeRepository derivativeRepository;
    private WindowDressingRepository windowDressingRepository;
    private String sqldbUrl;
    private String chartStartDate;
    private ControllerHubListener listener;
    private IntegerProperty shiftAmountProperty = new SimpleIntegerProperty(6);
    private BooleanProperty shiftBothChartsProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isShiftDaysProperty = new SimpleBooleanProperty(true);

    public MaunaloaChart getCandlesticksChart() {
        return candlesticksChart;
    }

    public void setCandlesticksChart(MaunaloaChart candlesticksChart) {
        this.candlesticksChart = candlesticksChart;
    }

    public MaunaloaChart getWeeklyChart() {
        return weeklyChart;
    }

    public void setWeeklyChart(MaunaloaChart weeklyChart) {
        this.weeklyChart = weeklyChart;
    }

    public MaunaloaChart getObxCandlesticksChart() {
        return obxCandlesticksChart;
    }

    public void setObxCandlesticksChart(MaunaloaChart obxCandlesticksChart) {
        this.obxCandlesticksChart = obxCandlesticksChart;
    }

    public MaunaloaChart getObxWeeklyChart() {
        return obxWeeklyChart;
    }

    public void setObxWeeklyChart(MaunaloaChart obxWeeklyChart) {
        this.obxWeeklyChart = obxWeeklyChart;
    }

    @Override
    public StockMarketRepository getStockRepository() {
        return stockRepository;
    }

    public void setStockRepository(StockMarketRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public DerivativeRepository getDerivativeRepository() {
        return derivativeRepository;
    }

    public void setDerivativeRepository(DerivativeRepository derivativeRepository) {
        this.derivativeRepository = derivativeRepository;
    }

    public void setWindowDressingRepository(WindowDressingRepository windowDressingRepository) {
        this.windowDressingRepository = windowDressingRepository;
    }
    @Override
    public WindowDressingRepository getWindowDressingRepository() {
        return windowDressingRepository;
    }

    public void setSqldbUrl(String sqldbUrl) {
        this.sqldbUrl = sqldbUrl;
    }

    public MaunaloaChart getOsebxCandlesticksChart() {
        return osebxCandlesticksChart;
    }

    public void setOsebxCandlesticksChart(MaunaloaChart osebxCandlesticksChart) {
        this.osebxCandlesticksChart = osebxCandlesticksChart;
    }

    public MaunaloaChart getOsebxWeeklyChart() {
        return osebxWeeklyChart;
    }

    public void setOsebxWeeklyChart(MaunaloaChart osebxWeeklyChart) {
        this.osebxWeeklyChart = osebxWeeklyChart;
    }

    public void setChartStartDate(String chartStartDate) {
        this.chartStartDate = chartStartDate;
    }

    public void setListener(ControllerHubListener listener) {
        this.listener = listener;
    }

    //endregion Properties

    //region REPL Demo Methods
    /*
    public java.util.List<javafx.scene.Node> fetchNodes(String ticker, int location) {
        oahu.domain.Tuple<oahux.chart.IRuler> rulers = new oahu.domain.Tuple<>(
                                                maunaloax.views.chart.DefaultDateRuler.createDummy(),
                                                maunaloax.views.chart.DefaultVRuler.createDummy());
        java.util.List<maunaloa.views.charts.ChartItem> items =
            windowDressingRepository.fetchFibLines(ticker,location,0,rulers);
        return items.stream().map(maunaloa.views.charts.ChartItem::view).collect(java.util.stream.Collectors.toList());
    }


    //*/
    //endregion REPL Demo Methods
}

