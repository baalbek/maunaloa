package maunaloa.controllers.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import maunaloa.controllers.DerivativesController;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.controllers.ChartViewModel;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DerivativesControllerImpl2 implements DerivativesController, ChartViewModel {
    //region FXML
    @FXML private Pane paneCandlesticks;
    @FXML private VBox containerCandlesticks;
    @FXML private Canvas myCanvas;

    public void calcRisk(ActionEvent event) {
        System.out.println("calcRisc fired! " + paneCandlesticks);
    }
    @Override
    public Collection<StockPrice> stockPrices(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getTicker() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IRuler getRuler(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setRuler(int i, IRuler iRuler) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void draw() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    //endregion FXML


    //region Init


    //endregion
}
