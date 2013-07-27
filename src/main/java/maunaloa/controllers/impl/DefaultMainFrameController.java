package maunaloa.controllers.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.controllers.DerivativesController;
import maunaloa.controllers.MainFrameController;
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
public class DefaultMainFrameController implements MainFrameController {
    //region FXML

    @FXML private ChartCanvasController candlesticksController;
    @FXML private ChartCanvasController weeksController;
    @FXML private DerivativesController optionsController;
    //endregion FXML

    //region Init

    private MaunaloaFacade facade;

    public DefaultMainFrameController() {
    }


    //endregion Init

    //region Public Methods

    public MaunaloaFacade getFacade() {
        return facade;
    }

    public void setFacade(MaunaloaFacade facade) {
        this.facade = facade;
    }

    public void close(ActionEvent event)  {
        System.exit(0);
    }

    //endregion  Public Methods

    //region Private Methods


    //endregion Private Methods



    //region Initialization methods
    public void initialize() {
        System.out.println("Candlesticks: " + candlesticksController);
        System.out.println("Weeks: " + weeksController);
        System.out.println("Options: " + optionsController);
    }



    //endregion  Initialization methods

    //region Properties

    //endregion
}
