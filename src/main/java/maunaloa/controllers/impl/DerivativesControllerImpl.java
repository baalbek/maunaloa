package maunaloa.controllers.impl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import maunaloa.controllers.DerivativesController;
import oahux.domain.DerivativeFx;
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
    @FXML private TableView<DerivativeFx> derivativesTableView;

    @FXML private TableColumn<DerivativeFx, String> colOpName;
    @FXML private TableColumn<DerivativeFx, Boolean> colSelected;
    @FXML private TableColumn<DerivativeFx, Date> colExpiry;

    @FXML private TableColumn<DerivativeFx, Double> colBuy;
    @FXML private TableColumn<DerivativeFx, Double> colSell;
    @FXML private TableColumn<DerivativeFx, Double> colIvBuy;
    @FXML private TableColumn<DerivativeFx, Double> colIvSell;
    @FXML private TableColumn<DerivativeFx, Double> colSpread;
    @FXML private TableColumn<DerivativeFx, Double> colDelta;
    @FXML private TableColumn<DerivativeFx, Double> colBreakEven;

    @FXML private TableColumn<DerivativeFx, Double> colDays;
    @FXML private TableColumn<DerivativeFx, Double> colRisc;
    @FXML private TableColumn<DerivativeFx, Double> colSpRisc;

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
        String txVal = ((TextField)event.getSource()).textProperty().get();
        double risk = Double.parseDouble(txVal);
        for (DerivativeFx fx : derivativesTableView.getItems()) {
            if (fx.isCheckedProperty().get() == true) {
                fx.setRisk(risk);
            }
        }
    }

    //endregion

    //region Private Methods

    private void loadCalls(String ticker) {
        if  (_selectedLoadDerivativesProperty.get() == true) {
            derivativesTableView.getItems().addAll(FXCollections.observableArrayList(model.calls(ticker)));
        }
        else if (_selectedLoadStockProperty.get() == true) {
        }
    }
    private void loadPuts(String ticker) {
        if  (_selectedLoadDerivativesProperty.get() == true) {
            derivativesTableView.getItems().addAll(FXCollections.observableArrayList(model.puts(ticker)));
        }
        else if (_selectedLoadStockProperty.get() == true) {
        }
    }
    private void loadAll() {

    }

    //endregion Private Methods

    //region Initialization methods
    public void initialize() {
        initGrid();
    }

    private void initGrid() {
        colOpName.setCellValueFactory(new PropertyValueFactory<DerivativeFx, String>("ticker"));


        colSelected.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Boolean>("isChecked"));
        colSelected.setCellFactory(
                new Callback<TableColumn<DerivativeFx, Boolean>, TableCell<DerivativeFx, Boolean>>() {
                    @Override
                    public TableCell<DerivativeFx, Boolean> call(TableColumn<DerivativeFx, Boolean> p) {
                        return new CheckBoxTableCell<>();
                    }
                });

        colRisc.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("risk"));
        colSpRisc.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("stockPriceRisk"));
        colExpiry.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Date>("expiry"));

        colBuy.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("buy"));
        colSell.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("sell"));

        colIvBuy.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("ivBuy"));
        colIvSell.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("ivSell"));
        colDays.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("days"));

        colDelta.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("delta"));
        colBreakEven.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("breakeven"));
        colSpread.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Double>("spread"));
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
        switch (_selectedDerivativeProperty.get().getUserData().toString()) {
            case "calls":
                loadCalls(ticker);
                break;
            case "puts":
                loadPuts(ticker);
                break;
            case "all":
                loadAll();
                break;
        }
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
