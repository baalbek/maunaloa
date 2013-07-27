package maunaloa.controllers.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import maunaloa.controllers.DerivativesController;
import oahu.financial.Derivative;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DerivativesControllerImpl implements DerivativesController {
    //region FXML
    @FXML private TableView<Derivative> derivativesTableView;

    @FXML private TableColumn<Derivative, String> colOpName;
    @FXML private TableColumn<Derivative, Boolean> colSelected;
    @FXML private TableColumn<Derivative, Date> colExpiry;

    @FXML private TableColumn<Derivative, Double> colBuy;
    @FXML private TableColumn<Derivative, Double> colSell;
    @FXML private TableColumn<Derivative, Double> colIvBuy;
    @FXML private TableColumn<Derivative, Double> colIvSell;
    @FXML private TableColumn<Derivative, Double> colSpread;
    @FXML private TableColumn<Derivative, Double> colDelta;
    @FXML private TableColumn<Derivative, Double> colBreakEven;

    @FXML private TableColumn<Derivative, Double> colDays;
    @FXML private TableColumn<Derivative, Double> colRisc;
    @FXML private TableColumn<Derivative, Double> colSpRisc;

    @FXML private TextField txSpot;
    @FXML private TextField txOpen;
    @FXML private TextField txHi;
    @FXML private TextField txLo;
    //endregion FXML

    //region Init

    public DerivativesControllerImpl() {
    }



    //endregion Init

    //region Public Methods

    public void calcRisk(ActionEvent event) {
        /*
        String txVal = ((TextField)event.getSource()).textProperty().get();
        double risk = Double.parseDouble(txVal);

        for (Derivative b : derivativesTableView.getItems()) {
            Derivative cb = (Derivative)b;
            if (cb.getIsChecked()) {
                cb.setRisk(risk);
            }
        }
        */

    }

    //endregion  Public Methods

    //region Private Methods


    //endregion Private Methods

    //region Fibonacci

    //endregion Fibonacci

    //region Interface methods

    //endregion  Interface methods

    //region Initialization methods
    public void initialize() {
    }



    //endregion  Initialization methods

    //region Properties

    //endregion
}
