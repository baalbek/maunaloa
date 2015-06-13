package maunaloa.controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import maunaloa.repository.DerivativeRepository;
import maunaloa.repository.WindowDressingRepository;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahu.financial.repository.StockMarketRepository;
import oahu.functional.Procedure0;
import oahu.functional.Procedure4;
import oahu.functional.Procedure5;
import oahux.chart.MaunaloaChart;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by rcs on 4/12/14.
 */
public class MainframeController implements ControllerHub {
    //region FXML
    @FXML private ChartCanvasController candlesticksController;
    /*
    @FXML private ChartCanvasController osebxCandlesticksController;
    @FXML private ChartCanvasController osebxWeeksController;
    @FXML private ChartCanvasController obxCandlesticksController;
    @FXML private ChartCanvasController obxWeeksController;
    @FXML private ChartCanvasController weeksController;
    */
    @FXML private DerivativesController optionsController;
    @FXML private ChoiceBox cbTickers;
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

   /* @FXML private MenuBar myMenuBar;
    @FXML private Menu linesMenu;
    @FXML private Menu mongodbMenu;
    */

    //endregion FXML

    //region Private Methods
    private Optional<ChartCanvasController> currentController() {
        int index =  myTabPane.getSelectionModel().getSelectedIndex();
        ChartCanvasController result = null;
        switch (index) {
            case 1: result = candlesticksController;
                break;
            /* --->>>
            case 2: result = weeksController;
                break;
            case 3: result = obxCandlesticksController;
                break;
            case 4: result = obxWeeksController;
                break;
            case 5: result = osebxCandlesticksController;
                break;
            case 6: result = osebxWeeksController;
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
    public void onShiftToEnd(ActionEvent event) {
        currentController().ifPresent(ChartCanvasController::shiftToEnd);
    }
    public void onShiftToDate(ActionEvent event) {

    }
    //endregion Events

    //region Initialize
    public void initialize() {
        initContextInfo();
        initNavButtons();
        initChoiceBoxTickers();
        LocalDate csd = LocalDate.of(2012,1,1);
        Procedure5<ChartCanvasController,String,Integer,Integer,MaunaloaChart> initController =
                (ChartCanvasController controller,
                                String name,
                                Integer location,
                                Integer numShiftWeeks,
                                MaunaloaChart chart) -> {
                    controller.setName(name);
                    controller.setLocation(location);
                    controller.setNumShiftWeeks(numShiftWeeks);
                    controller.setChart(chart);
                    controller.setHub(this);
                    controller.setChartStartDate(csd);
                    System.out.println("Setting up controller " + name);
                };


        initController.apply(candlesticksController, "Candlesticks", 1, 4, candlesticksChart);
        /*--->>>
        initController.apply(weeksController, "Weeks", 2, weeklyChart);
        initController.apply(obxCandlesticksController, "OBX Candlest.", 3, obxCandlesticksChart);
        initController.apply(obxWeeksController, "OBX Weeks", 4, obxWeeklyChart);
        initController.apply(osebxCandlesticksController, "OSEBX Candlest.", 5, osebxCandlesticksChart);
        initController.apply(osebxWeeksController, "OSEBX Weeks", 6, osebxWeeklyChart);
        */

        initOptionsController();

    }

    private void initOptionsController() {

        Procedure0 setStock = () -> {
            Object prop = cbTickers.valueProperty().get();
            if (prop == null) return;
            Stock stock = (Stock)prop;
            System.out.println(stock.getTicker());
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
            /*
            optionsController.addDerivativesControllerListener(candlesticksController);
            optionsController.addDerivativesControllerListener(weeksController);
            */
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
            currentController().ifPresent(ChartCanvasController::shiftLeft);
        });
        btnShiftRight.setOnAction(e -> {
            currentController().ifPresent(ChartCanvasController::shiftRight);
        });
    }
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
                    //--->>> weeksController.setStock(s);
                    break;
                case 2:
                    //--->>> osebxCandlesticksController.setStock(s);
                    //--->>> osebxWeeksController.setStock(s);
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

