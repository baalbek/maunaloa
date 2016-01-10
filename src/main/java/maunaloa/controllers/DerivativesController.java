package maunaloa.controllers;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import javafx.beans.binding.Bindings;
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
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import maunaloa.derivatives.PurchaseCategory;
import maunaloa.derivatives.RiscItem;
import maunaloa.repository.DerivativeRepository;
import maunaloa.stocks.StockPriceFx;
import oahu.financial.Stock;
import oahux.financial.DerivativeFx;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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
            calcRisc(cbitems.get(newValue.intValue()));
        });
        txRisc.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double newRiscValue = Double.parseDouble(newValue.replace(",", "."));
                if (newRiscValue > 0) {
                    calcRisc(new RiscItem(newRiscValue));
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


    //region Private Methods
    private void calcRisc(RiscItem riscItem) {
        /*
        fireCalculatedEvent(getSelectedDerivatives(fx -> {
            fx.setRisk(riscItem.getValue());
        }));
        */
    }
    private void _updateDerivatives() {
        String ticker = this.stock.getTicker();
        DerivativeRepository derivativeRepository = mainframeController.getDerivativeRepository();
        try {
            switch (_selectedDerivativeProperty.get().getUserData().toString()) {
                case "calls":
                    log.info(String.format("Fetching calls for %s", ticker));
                    load((s) -> derivativeRepository.getCalls(s), ticker);
                    break;
                case "puts":
                    log.info(String.format("Fetching puts for %s", ticker));
                    load((s) -> derivativeRepository.getPuts(s), ticker);
                    break;
            }
            if (_selectedLoadStockProperty.get()) {
                derivativeRepository.getSpot(ticker).ifPresent(spot -> {
                    stockPrice.setPrice(spot);
                    //===>>> fireAssignStockPriceEvent(spot);
                });
            }
        }
        catch (FailingHttpStatusCodeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid URL");
            alert.setContentText(String.format("Url for %s not valid: %s", ticker, ex.getStatusMessage()));
            alert.showAndWait();
        }
    }
    private void load(Function<String,Collection<DerivativeFx>> action, String ticker) {
        if  (_selectedLoadDerivativesProperty.get()) {
            ObservableList<DerivativeFx> items = FXCollections.observableArrayList(action.apply(ticker));
            derivativesTableView.getItems().setAll(items);
        }
    }
    //endregion Private Methods

    //region Properties

    private MainframeController mainframeController;
    private BooleanProperty _selectedLoadStockProperty = new SimpleBooleanProperty();
    private BooleanProperty _selectedLoadDerivativesProperty = new SimpleBooleanProperty();
    private ObjectProperty<Toggle> _selectedDerivativeProperty = new SimpleObjectProperty<>();
    private StockPriceFx stockPrice = new StockPriceFx();
    private Stock stock;

    private nz.sodium.Cell<Stock> stockPriceCell;

    public void setStockPriceStream(nz.sodium.Stream<Stock> stockPriceStream) {
        this.stockPriceCell = stockPriceStream.hold(null);
        this.stockPriceCell.listen( s -> {
            if (s == null) {
                log.warn("Stock was null");
                return;
            }
            stock = s;
            _updateDerivatives();
        });
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

    public void setMainframeController(MainframeController mainframeController) {
        this.mainframeController = mainframeController;
    }

    //endregion Properties
}
