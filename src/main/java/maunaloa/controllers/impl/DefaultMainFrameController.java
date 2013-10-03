package maunaloa.controllers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.controllers.DerivativesController;
import maunaloa.controllers.MainFrameController;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahux.chart.MaunaloaChart;
import maunaloa.models.MaunaloaFacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DefaultMainFrameController implements MainFrameController {
    //region FXML
    @FXML private ChartCanvasController obxCandlesticksController;
    @FXML private ChartCanvasController obxWeeksController;
    @FXML private ChartCanvasController candlesticksController;
    @FXML private ChartCanvasController weeksController;
    @FXML private DerivativesController optionsController;
    @FXML private ChoiceBox cbTickers;
    @FXML private MenuBar myMenuBar;
    @FXML private Menu fibonacciMenu;
    @FXML private Menu mongodbMenu;
    @FXML private ToggleGroup rgDerivatives;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;
    //endregion FXML

    //region Init

    private MaunaloaFacade facade;
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;
    private MaunaloaChart obxWeeklyChart;
    private MaunaloaChart obxCandlesticksChart;
    private CheckMenuItem fib1272extCheckMenu;


    public DefaultMainFrameController() {

    }


    //endregion Init

    //region FXML Actions

    public void close(ActionEvent event)  {
        System.exit(0);
    }

    //endregion

    //region Initialization methods

    public List<Stock> getTickers() {
        return facade.getTickers();
    }

    public void setTicker(Stock ticker) {

        switch (ticker.getTickerCategory()) {
            case 1:
                candlesticksController.setTicker(ticker);
                weeksController.setTicker(ticker);
                optionsController.setTicker(ticker);
                break;
            case 2:
                obxCandlesticksController.setTicker(ticker);
                obxWeeksController.setTicker(ticker);
                break;
        }
        /*
        if (cxLoadStockHtml.isSelected()) {
            stock.assign(facade.spot(ticker));
        }
        draw();
        */
    }

    private void initChoiceBoxTickers() {
        final ObservableList<Stock> cbitems = FXCollections.observableArrayList(getTickers());
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
        cbTickers.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                        setTicker(cbitems.get(newValue.intValue()));
                    }
                }
        );
    }

    private void notifyOptionsController() {
        Object prop = cbTickers.valueProperty().get();
        if (prop == null) return;
        Stock ticker = (Stock)prop;
        optionsController.setTicker(ticker);
    }

    private void initOptionsController() {
        rgDerivatives.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue,
                                Toggle toggle,
                                Toggle toggle2) {
                notifyOptionsController();
            }
        });

        cxLoadOptionsHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadOptionsHtml.isSelected()) {
                    notifyOptionsController();
                }
            }
        });

        cxLoadStockHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadStockHtml.isSelected()) {
                    notifyOptionsController();
                }
            }
        });

        optionsController.selectedDerivativeProperty().bind(rgDerivatives.selectedToggleProperty());
        optionsController.selectedLoadStockProperty().bind(cxLoadStockHtml.selectedProperty());
        optionsController.selectedLoadDerivativesProperty().bind(cxLoadOptionsHtml.selectedProperty());
        optionsController.setModel(getFacade());
        optionsController.addDerivativesCalculatedListener(candlesticksController);
        optionsController.addDerivativesCalculatedListener(weeksController);
    }

    public void initialize() {

        initChoiceBoxTickers();

        Map<String, Menu> myMenus = new HashMap<>();
        myMenus.put("fibonacci",fibonacciMenu);
        fib1272extCheckMenu = new CheckMenuItem("1.272 extension");
        fib1272extCheckMenu.setSelected(true);
        fibonacciMenu.getItems().addAll(fib1272extCheckMenu, new SeparatorMenuItem());

        myMenus.put("mongodb",mongodbMenu);

        initCanvanController(candlesticksController,"Candlesticks",1,getCandlesticksChart(),myMenus);
        initCanvanController(weeksController,"Weeks",2,getWeeklyChart(),myMenus);
        initCanvanController(obxCandlesticksController,"OBX Candlest.",3,getObxCandlesticksChart(),myMenus);
        initCanvanController(obxWeeksController, "OBX Weeks", 4, getObxWeeklyChart(), myMenus);

        initOptionsController();
    }

    private void initCanvanController(ChartCanvasController controller,
                                      String name,
                                      int location,
                                      MaunaloaChart chart,
                                      Map<String, Menu> menus) {

        controller.setName(name);
        controller.setLocation(location);
        controller.setChart(chart);
        controller.setModel(getFacade());
        controller.setMenus(menus);
        controller.fibonacci1272extProperty().bind(fib1272extCheckMenu.selectedProperty());

    }

    //endregion  Initialization methods

    //region Properties

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


    public MaunaloaFacade getFacade() {
        return facade;
    }

    public void setFacade(MaunaloaFacade facade) {
        this.facade = facade;
    }

    public MaunaloaChart getObxWeeklyChart() {
        return obxWeeklyChart;
    }

    public void setObxWeeklyChart(MaunaloaChart obxWeeklyChart) {
        this.obxWeeklyChart = obxWeeklyChart;
    }

    public MaunaloaChart getObxCandlesticksChart() {
        return obxCandlesticksChart;
    }

    public void setObxCandlesticksChart(MaunaloaChart obxCandlesticksChart) {
        this.obxCandlesticksChart = obxCandlesticksChart;
    }

    //endregion

    //region Interface Methods
    //endregion
}
