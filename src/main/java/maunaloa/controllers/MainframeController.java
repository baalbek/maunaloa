package maunaloa.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import oahu.financial.Stock;
import oahu.functional.Procedure0;
import oahu.functional.Procedure4;
import oahux.chart.MaunaloaChart;

/**
 * Created by rcs on 4/12/14.
 */
public class MainframeController {
    //region FXML
    @FXML private ChartCanvasController obxCandlesticksController;
    @FXML private ChartCanvasController obxWeeksController;
    @FXML private ChartCanvasController candlesticksController;
    @FXML private ChartCanvasController weeksController;
    @FXML private DerivativesController optionsController;
    @FXML private ChoiceBox cbTickers;
    @FXML private MenuBar myMenuBar;
    @FXML private Menu linesMenu;
    @FXML private Menu mongodbMenu;
    @FXML private ToggleGroup rgDerivatives;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;
    @FXML private TabPane myTabPane;
    @FXML private CheckBox cxIsCloud;
    @FXML private Label lblLocalMongodbUrl;
    @FXML private Label lblSqlUrl;
    //endregion FXML

    //region Events
    public void close(ActionEvent event)  {
        System.exit(0);
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
                    //controller.setModel(facade);
                    System.out.println("Setting up controller " + name);
                };


        initController.call(candlesticksController, "Candlesticks", 1, candlesticksChart);
        initController.call(weeksController,"Weeks",2,weeklyChart);
        initController.call(obxCandlesticksController,"OBX Candlest.",3,obxCandlesticksChart);
        initController.call(obxWeeksController, "OBX Weeks", 4, obxWeeklyChart);

        initOptionsController();
    }

    private void initOptionsController() {
        Procedure0 notifyOptionsController = () -> {
            Object prop = cbTickers.valueProperty().get();
            if (prop == null) return;
            Stock stock = (Stock)prop;
            System.out.println(stock.getTicker());
            //optionsController.setStock(stock);
        };

        rgDerivatives.selectedToggleProperty().addListener(e -> {
            notifyOptionsController.call();
        });
        cxLoadOptionsHtml.selectedProperty().addListener( e -> {
            notifyOptionsController.call();
        });
        cxLoadStockHtml.selectedProperty().addListener( e -> {
            notifyOptionsController.call();
        });

        if (optionsController != null) {
            /*
            optionsController.selectedDerivativeProperty().bind(rgDerivatives.selectedToggleProperty());
            optionsController.selectedLoadStockProperty().bind(cxLoadStockHtml.selectedProperty());
            optionsController.selectedLoadDerivativesProperty().bind(cxLoadOptionsHtml.selectedProperty());
            optionsController.setModel(facade);
            optionsController.addDerivativesControllerListener(candlesticksController);
            optionsController.addDerivativesControllerListener(weeksController);
            */
        }
    }
    private void initChoiceBoxTickers() {
        //final ObservableList<Stock> cbitems = FXCollections.observableArrayList(facade.getStocks());
    }
    //endregion Initialize


    //region Properties
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;
    private MaunaloaChart obxCandlesticksChart;
    private MaunaloaChart obxWeeklyChart;

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

    //endregion Properties
}

