package maunaloa.controllers.impl;

import javafx.event.ActionEvent;
import maunaloa.controllers.DerivativesController;
import oahu.exceptions.NotImplementedException;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.controllers.ChartViewModel;
import oahux.models.MaunaloaFacade;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DerivativesControllerImpl implements DerivativesController, ChartViewModel {
    //region FXML

    //endregion FXML

    //region Init

    private MaunaloaFacade facade;

    public DerivativesControllerImpl() {
    }

    public DerivativesControllerImpl(MaunaloaFacade facade) {
        this.facade = facade;
    }



    //endregion Init

    //region Public Methods

    public void close(ActionEvent event)  {
        System.exit(0);
    }

    //endregion  Public Methods

    //region Private Methods


    //endregion Private Methods

    //region Fibonacci

    //endregion Fibonacci

    //region Interface methods

    @Override
    public Collection<StockPrice> stockPrices(int i) {
        throw new NotImplementedException();
    }

    @Override
    public String getTicker() {
        throw new NotImplementedException();
    }

    @Override
    public IRuler getRuler(int i) {
        throw new NotImplementedException();
    }

    @Override
    public void setRuler(int i, IRuler iRuler) {
        throw new NotImplementedException();
    }

    @Override
    public void draw() {
    }


    //endregion  Interface methods

    //region Initialization methods
    public void initialize() {
    }



    //endregion  Initialization methods

    //region Properties

    //endregion
}
