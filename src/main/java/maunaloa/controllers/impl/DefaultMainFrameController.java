package maunaloa.controllers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.controllers.DerivativesController;
import maunaloa.controllers.MainFrameController;
import oahu.exceptions.NotImplementedException;
import oahu.financial.StockLocator;
import oahu.financial.StockPrice;
import oahux.chart.MaunaloaChart;
import oahux.chart.IRuler;
import oahux.models.MaunaloaFacade;

import java.util.Collection;
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

    //region Private Methods


    public List<String> getTickers() {
        return facade.getTickers();
    }

    public void setTicker(String ticker) {
        candlesticksController.setTicker(ticker);
        weeksController.setTicker(ticker);
        /*
        this.ticker = ticker;
        if (cxLoadOptionsHtml.isSelected()) {
            ObservableList<Derivative> items = derivatives();
            if (items != null) {
                derivativesTableView.getItems().setAll(items);
            }
        }
        if (cxLoadStockHtml.isSelected()) {
            stock.assign(facade.spot(ticker));
        }
        draw();
        */
    }

    private void initChoiceBoxTickers() {
        final ObservableList<String> cbitems = FXCollections.observableArrayList();
        for (String s : getTickers()) {
            cbitems.add(s);
        }
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

    //endregion Private Methods

    //region Initialization methods
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
