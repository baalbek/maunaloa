package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import maunaloa.repository.DerivativeRepository;
import oahu.financial.Derivative;
import oahu.financial.Stock;
import oahux.financial.DerivativeFx;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by rcs on 4/13/14.
 */
public class DerivativesController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    //region FXML
    @FXML
    private TableView<DerivativeFx> derivativesTableView;
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

    @FXML private ChoiceBox cbRisc;
    @FXML private TextField txSpot;
    @FXML private TextField txOpen;
    @FXML private TextField txHi;
    @FXML private TextField txLo;
    //endregion FXML

    private DerivativeRepository derivativeRepository;
    private ObjectProperty<Toggle> _selectedDerivativeProperty = new SimpleObjectProperty<Toggle>();
    private BooleanProperty _selectedLoadStockProperty = new SimpleBooleanProperty();
    private BooleanProperty _selectedLoadDerivativesProperty = new SimpleBooleanProperty();

    //region Initialization methods
    public void initialize() {
        //initChoiceBoxRisc();
        initGrid();
        //initStockPrice();
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
    //endregion Initialization methods

    public void setStock(Stock stock) {
        if (stock == null) {
            log.warn("Stock was null");
            return;
        }

        /*
        Consumer<String> loadCalls = (String ticker) -> {

        };
        */
        String ticker = stock.getTicker();

        switch (_selectedDerivativeProperty.get().getUserData().toString()) {
            case "calls":
                log.info(String.format("Fetching calls for %s",ticker));
                load((s) -> { return derivativeRepository.calls(s); }, ticker);
                break;
            case "puts":
                log.info(String.format("Fetching puts for %s",ticker));
                load((s) -> { return derivativeRepository.puts(s); }, ticker);
                break;
            case "all":
                //loadAll();
                break;
        }
    }
    private void load(Function<String,Collection<DerivativeFx>> action,String ticker) {
        if  (_selectedLoadDerivativesProperty.get() == true) {
            ObservableList<DerivativeFx> items = FXCollections.observableArrayList(action.apply(ticker));
            derivativesTableView.getItems().setAll(items);
        }
    }

    //region Properties

    public void setDerivativeRepository(DerivativeRepository derivativeRepository) {
        this.derivativeRepository = derivativeRepository;
    }

    public ObjectProperty<Toggle> selectedDerivativeProperty() {
        return _selectedDerivativeProperty;
    }

    public BooleanProperty selectedLoadStockProperty() {
        return _selectedLoadStockProperty;
    }

    public BooleanProperty selectedLoadDerivativesProperty() {
        return _selectedLoadDerivativesProperty;
    }
    //endregion Properties
}
