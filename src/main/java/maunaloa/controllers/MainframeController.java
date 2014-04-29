package maunaloa.controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import maunaloa.repository.DerivativeRepository;
import maunaloa.repository.StockRepository;
import maunaloa.repository.WindowDressingRepository;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahu.functional.Procedure0;
import oahu.functional.Procedure4;
import oahux.chart.MaunaloaChart;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by rcs on 4/12/14.
 */
public class MainframeController implements ControllerHub {
    //region FXML
    @FXML private ChartCanvasController obxCandlesticksController;
    @FXML private ChartCanvasController obxWeeksController;
    @FXML private ChartCanvasController candlesticksController;
    @FXML private ChartCanvasController weeksController;
    @FXML private DerivativesController optionsController;
    @FXML private ChoiceBox cbTickers;
    @FXML private ToggleGroup rgDerivatives;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;
    @FXML private TabPane myTabPane;
    @FXML private CheckBox cxIsCloud;
    @FXML private Label lblLocalMongodbUrl;
    @FXML private Label lblSqlUrl;

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
            case 2: result = weeksController;
                break;
            case 3: result = obxCandlesticksController;
                break;
            case 4: result = obxWeeksController;
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
    public void onFibLinesFromRepos(ActionEvent event) {
        currentController().ifPresent(ChartCanvasController::onFibLinesFromRepos);
    }
    //endregion Events

    //region Initialize
    public void initialize() {
        initChoiceBoxTickers();
        Procedure4<ChartCanvasController,String,Integer,MaunaloaChart>  initController =
                (ChartCanvasController controller,
                                String name,
                                Integer location,
                                MaunaloaChart chart) -> {
                    controller.setName(name);
                    controller.setLocation(location);
                    controller.setChart(chart);
                    controller.setHub(this);
                    System.out.println("Setting up controller " + name);
                };


        initController.apply(candlesticksController, "Candlesticks", 1, candlesticksChart);
        initController.apply(weeksController, "Weeks", 2, weeklyChart);
        initController.apply(obxCandlesticksController, "OBX Candlest.", 3, obxCandlesticksChart);
        initController.apply(obxWeeksController, "OBX Weeks", 4, obxWeeklyChart);

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
            System.out.println(s.getCompanyName());
            currentStock = s;
            switch (s.getTickerCategory()) {
                case 1:
                    candlesticksController.setStock(s);
                    weeksController.setStock(s);
                    optionsController.setStock(s);
                    break;
                case 2:
                    obxCandlesticksController.setStock(s);
                    obxWeeksController.setStock(s);
                    break;
            }
        };

        cbTickers.getSelectionModel().selectedIndexProperty().addListener(
            (ObservableValue<? extends Number> observableValue, Number value, Number newValue) -> {
                setStock.accept(cbitems.get(newValue.intValue()));
        });
    }
    //endregion Initialize

    //region Properties
    private Stock currentStock;
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;
    private MaunaloaChart obxCandlesticksChart;
    private MaunaloaChart obxWeeklyChart;
    private StockRepository stockRepository;
    private DerivativeRepository derivativeRepository;
    private WindowDressingRepository windowDressingRepository;

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
    public StockRepository getStockRepository() {
        return stockRepository;
    }

    public void setStockRepository(StockRepository stockRepository) {
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

    //endregion Properties

    //region REPL Demo Methods
    //*
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

