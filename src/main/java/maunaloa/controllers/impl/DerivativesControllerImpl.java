package maunaloa.controllers.impl;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import maunaloa.controllers.DerivativesController;
import oahu.financial.Derivative;
import oahux.models.MaunaloaFacade;

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
    private MaunaloaFacade model;
    private MenuBar menuBar;
    //endregion FXML

    //region Init

    public DerivativesControllerImpl() {
    }
    //endregion Init

    //region FXML Actions

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

    //endregion

    //region Private Methods


    //endregion Private Methods

    //region Initialization methods
    public void initialize() {
        initGrid();
    }

    private void initGrid() {
        colOpName.setCellValueFactory(new PropertyValueFactory<Derivative, String>("ticker"));
        colSelected.setCellValueFactory(new PropertyValueFactory<Derivative, Boolean>("isChecked"));
        colSelected.setCellFactory(
                new Callback<TableColumn<Derivative, Boolean>, TableCell<Derivative, Boolean>>() {
                    @Override
                    public TableCell<Derivative, Boolean> call(TableColumn<Derivative, Boolean> p) {
                        return new CheckBoxTableCell<>();
                    }
                });
        colExpiry.setCellValueFactory(new PropertyValueFactory<Derivative, Date>("expiry"));

        colBuy.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("buy"));
        colSell.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("sell"));

        colIvBuy.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("ivBuy"));
        colIvSell.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("ivSell"));
        colDelta.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("delta"));
        colBreakEven.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("breakeven"));
        colSpread.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("spread"));
        colDays.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("days"));
        colRisc.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("risk"));
        colSpRisc.setCellValueFactory(new PropertyValueFactory<Derivative, Double>("stockPriceRisk"));
    }

    //endregion  Initialization methods

    //region Interface methods

    private ObjectProperty<Toggle> _selectedDerivativeProperty = new SimpleObjectProperty<Toggle>();
    @Override
    public ObjectProperty<Toggle> selectedDerivativeProperty() {
        return _selectedDerivativeProperty;
    }


    private BooleanProperty _selectedLoadStockProperty = new SimpleBooleanProperty();
    @Override
    public BooleanProperty selectedLoadStockProperty() {
        return _selectedLoadStockProperty;
    }

    private BooleanProperty _selectedLoadDerivativesProperty = new SimpleBooleanProperty();
    @Override
    public BooleanProperty selectedLoadDerivativesProperty() {
        return _selectedLoadDerivativesProperty;
    }

    @Override
    public void setTicker(String ticker) {
        System.out.println(_selectedDerivativeProperty.get().getUserData());
        System.out.println(_selectedLoadStockProperty.get());
        System.out.println(_selectedLoadDerivativesProperty.get());
    }

    @Override
    public void setModel(MaunaloaFacade model) {
        this.model = model;
    }

    @Override
    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }
    //endregion  Interface methods

    //region Properties

    //endregion
}
