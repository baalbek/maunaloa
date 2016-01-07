package maunaloa.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import maunaloa.derivatives.PurchaseCategory;
import maunaloa.derivatives.RiscItem;
import maunaloa.stocks.StockPriceFx;
import oahux.financial.DerivativeFx;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcs on 06.01.16.
 *
 */
public class DerivativesController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    //region FXML
    @FXML
    private TableView<DerivativeFx> derivativesTableView;
    @FXML
    private TableColumn<DerivativeFx, String> colOpName;
    @FXML
    private TableColumn<DerivativeFx, Integer> colOid;
    @FXML
    private TableColumn<DerivativeFx, Boolean> colSelected;
    /*
    @FXML
    private TableColumn<DerivativeFx, Date> colExpiry;
    */

    @FXML
    private TableColumn<DerivativeFx, Double> colBuy;
    @FXML
    private TableColumn<DerivativeFx, Double> colSell;
    @FXML
    private TableColumn<DerivativeFx, Double> colIvBuy;
    @FXML
    private TableColumn<DerivativeFx, Double> colIvSell;
    @FXML
    private TableColumn<DerivativeFx, Double> colSpread;
    @FXML
    private TableColumn<DerivativeFx, Double> colDelta;
    @FXML
    private TableColumn<DerivativeFx, Double> colBreakEven;

    @FXML
    private TableColumn<DerivativeFx, Double> colDays;
    @FXML
    private TableColumn<DerivativeFx, Double> colRisc;
    @FXML
    private TableColumn<DerivativeFx, Double> colSpRisc;

    @FXML
    private Button btnOptionPurchase;
    @FXML
    private Button btnSpotOpts;
    @FXML
    private Button btnOptionPriceSliders;
    @FXML
    private ChoiceBox cbRisc;
    @FXML
    private ChoiceBox cbPurchaseType;
    @FXML
    private TextField txPurchaseAmount;
    @FXML
    private TextField txRisc;
    @FXML
    private TextField txSpot;
    @FXML
    private TextField txOpen;
    @FXML
    private TextField txHi;
    @FXML
    private TextField txLo;
    //endregion FXML

    //region Initialization methods
    public void initialize() {
        initChoiceBoxRisc();
        initChoiceBoxPurchaseCategory();
        initGrid();
    }

    private List<RiscItem> riscItems() {
        List<RiscItem> result = new ArrayList<>();

        for (int i=1; i<11; ++i) {
            result.add(new RiscItem(i));
            result.add(new RiscItem(i+0.5));
        }
        for (int i=11; i<25; ++i) {
            result.add(new RiscItem(i));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void initChoiceBoxRisc() {
        final ObservableList<RiscItem> cbitems = FXCollections.observableArrayList(riscItems());

        cbRisc.getItems().addAll(cbitems);
        cbRisc.getSelectionModel().selectedIndexProperty().addListener((observable, value, newValue) -> {
            //calcRisc(cbitems.get(newValue.intValue()));
        });
        txRisc.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double newRiscValue = Double.parseDouble(newValue.replace(",", "."));
                if (newRiscValue > 0) {
                    //calcRisc(new RiscItem(newRiscValue));
                }
            } catch (Exception ex) {
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initChoiceBoxPurchaseCategory(){
        List<PurchaseCategory> items = new ArrayList<>();
        items.add(new PurchaseCategory(3,"Real Trade"));
        items.add(new PurchaseCategory(4, "Test Trade"));
        items.add(new PurchaseCategory(11, "Paper Trade"));
        final ObservableList<PurchaseCategory> cbitems = FXCollections.observableArrayList(items);
        cbPurchaseType.getItems().addAll(cbitems);
    }
    private void initGrid() {
        colOpName.setCellValueFactory(new PropertyValueFactory<>("ticker"));
        colOid.setCellValueFactory(new PropertyValueFactory<>("derivativeOid"));


        colSelected.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        colSelected.setCellFactory(
                p -> new CheckBoxTableCell<>());

        colRisc.setCellValueFactory(new PropertyValueFactory<>("risk"));
        colSpRisc.setCellValueFactory(new PropertyValueFactory<>("stockPriceRisk"));

        /* colExpiry.setCellValueFactory(new PropertyValueFactory<DerivativeFx, Date>("expiry")); */

        colBuy.setCellValueFactory(new PropertyValueFactory<>("buy"));
        colSell.setCellValueFactory(new PropertyValueFactory<>("sell"));

        colIvBuy.setCellValueFactory(new PropertyValueFactory<>("ivBuy"));
        colIvSell.setCellValueFactory(new PropertyValueFactory<>("ivSell"));
        colDays.setCellValueFactory(new PropertyValueFactory<>("days"));

        colDelta.setCellValueFactory(new PropertyValueFactory<>("delta"));
        colBreakEven.setCellValueFactory(new PropertyValueFactory<>("breakeven"));
        colSpread.setCellValueFactory(new PropertyValueFactory<>("spread"));
    }

    @SuppressWarnings("unchecked")
    private void initStockPrice() {
        StringConverter<? extends Number> converter =  new DoubleStringConverter();
        Bindings.bindBidirectional(txSpot.textProperty(), stockPrice.clsProperty(), (StringConverter<Number>) converter);
        Bindings.bindBidirectional(txOpen.textProperty(), stockPrice.opnProperty(),  (StringConverter<Number>)converter);
        Bindings.bindBidirectional(txHi.textProperty(), stockPrice.hiProperty(),  (StringConverter<Number>)converter);
        Bindings.bindBidirectional(txLo.textProperty(), stockPrice.loProperty(),  (StringConverter<Number>)converter);
    }
    //endregion Initialization methods

    //region Properties

    private StockPriceFx stockPrice = new StockPriceFx();
    //endregion Properties
}
