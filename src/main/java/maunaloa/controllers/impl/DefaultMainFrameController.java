package maunaloa.controllers.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.controllers.DerivativesController;
import maunaloa.controllers.MainFrameController;
import oahu.exceptions.NotImplementedException;
import oahu.financial.StockPrice;
import oahux.chart.MaunaloaChart;
import oahux.chart.IRuler;
import oahux.models.MaunaloaFacade;

import java.util.Collection;

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
    //endregion FXML

    //region Init

    private MaunaloaFacade facade;
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;

    public DefaultMainFrameController() {
    }


    //endregion Init

    //region FXML Actions

    public void activateFibA(ActionEvent event)  {
        candlesticksController.draw();
    }

    public void deactivateFibA(ActionEvent event)  {
    }

    public void clearFibA(ActionEvent event)  {
    }


    public void activateFibB(ActionEvent event)  {
        weeksController.draw();
    }

    public void deactivateFibB(ActionEvent event)  {
    }

    public void clearFibB(ActionEvent event)  {
    }

    public void selectAllDerivatives(ActionEvent event)  {
    }

    public void unSelectAllDerivatives(ActionEvent event)  {
    }


    public void unCheckBeans(ActionEvent event) {

    }

    public void close(ActionEvent event)  {
        System.exit(0);
    }

    //endregion

    //region Private Methods


    //endregion Private Methods



    //region Initialization methods
    public void initialize() {
        System.out.println("Candlesticks: " + candlesticksController);
        System.out.println("Weeks: " + weeksController);
        System.out.println("Options: " + optionsController);

        candlesticksController.setChart(getCandlesticksChart());
        candlesticksController.setModel(getFacade());

        weeksController.setChart(getWeeklyChart());
        weeksController.setModel(getFacade());
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
