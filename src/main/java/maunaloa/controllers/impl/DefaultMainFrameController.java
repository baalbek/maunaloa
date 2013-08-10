package maunaloa.controllers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.controllers.DerivativesController;
import maunaloa.controllers.MainFrameController;
import oahux.chart.MaunaloaChart;
import oahux.models.MaunaloaFacade;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DefaultMainFrameController implements MainFrameController {
    //region FXML
    @FXML private ChartCanvasController candlesticksController;
    @FXML private ChartCanvasController weeksController;
    @FXML private DerivativesController optionsController;
    @FXML private ChoiceBox cbTickers;
    @FXML private MenuBar myMenuBar;
    @FXML private ToggleGroup rgDerivatives;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;
    //endregion FXML

    //region Init

    private MaunaloaFacade facade;
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;

    public DefaultMainFrameController() {

    }


    //endregion Init

    //region FXML Actions

    public void close(ActionEvent event)  {
        System.exit(0);
    }

    //endregion

    //region Initialization methods

    public List<String> getTickers() {
        return facade.getTickers();
    }

    public void setTicker(String ticker) {
        candlesticksController.setTicker(ticker);
        weeksController.setTicker(ticker);
        optionsController.setTicker(ticker);
        /*
        if (cxLoadStockHtml.isSelected()) {
            stock.assign(facade.spot(ticker));
        }
        draw();
        */
        //System.out.println("Optionproperty: " + optionsController.selectedDerivativeProperty().getValue());
    }

    private void initChoiceBoxTickers() {
        final ObservableList<String> cbitems = FXCollections.observableArrayList(getTickers());
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
        String ticker = prop.toString();
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
        optionsController.setMenuBar(myMenuBar);
        optionsController.addDerivativesCalculatedListener(candlesticksController);
        optionsController.addDerivativesCalculatedListener(weeksController);
    }

    public void initialize() {

        initChoiceBoxTickers();

        candlesticksController.setName("Candlesticks");
        candlesticksController.setChart(getCandlesticksChart());
        candlesticksController.setModel(getFacade());
        candlesticksController.setMenuBar(myMenuBar);

        weeksController.setName("Weeks");
        weeksController.setChart(getWeeklyChart());
        weeksController.setModel(getFacade());
        weeksController.setMenuBar(myMenuBar);

        initOptionsController();
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

    //endregion

    //region Interface Methods
    //endregion
}
