package maunaloa.controllers;

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
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import maunaloa.financial.StockPriceFx;
import maunaloa.repository.DerivativeRepository;
import maunaloa.views.PurchaseCategory;
import maunaloa.views.RiscItem;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;
import org.apache.log4j.Logger;
import ranoraraku.beans.OptionPurchaseWithDerivativeBean;

import java.util.*;
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

    @FXML private Button btnOptionPurchase;
    @FXML private ChoiceBox cbRisc;
    @FXML private ChoiceBox cbPurchaseType;
    @FXML private TextField txPurchaseAmount;
    @FXML private TextField txRisc;
    @FXML private TextField txSpot;
    @FXML private TextField txOpen;
    @FXML private TextField txHi;
    @FXML private TextField txLo;
    //endregion FXML

    private DerivativeRepository derivativeRepository;
    private ObjectProperty<Toggle> _selectedDerivativeProperty = new SimpleObjectProperty<Toggle>();
    private BooleanProperty _selectedLoadStockProperty = new SimpleBooleanProperty();
    private BooleanProperty _selectedLoadDerivativesProperty = new SimpleBooleanProperty();
    private List<DerivativesControllerListener> calculatedListeners;
    private StockPriceFx stockPrice = new StockPriceFx();
    private Stock stock;

    //region Initialization methods
    public void initialize() {
        initChoiceBoxRisc();
        initChoiceBoxPurchaseCategory();
        initGrid();
        initStockPrice();
        btnOptionPurchase.setOnAction(event -> {
            PurchaseCategory cat =
                    (PurchaseCategory)cbPurchaseType.getSelectionModel().selectedItemProperty().get();

            if (cat == null) return;

            int purchaseAmount = Integer.parseInt(txPurchaseAmount.getText());
            //java.util.Date dx = new java.util.Date();

            for (DerivativeFx fx : derivativesTableView.getItems()) {
                if (fx.isCheckedProperty().get() == true) {

                    /*
                    OptionPurchaseWithDerivativeBean purchase = new OptionPurchaseWithDerivativeBean();
                    purchase.setDerivative(fx);
                    purchase.setStatus(1);
                    purchase.setDx(new java.util.Date());
                    purchase.setVolume(Integer.parseInt(txPurchaseAmount.getText()));
                    purchase.setPurchaseType(cat.getValue());
                    purchase.setSpotAtPurchase(Double.parseDouble(txSpot.getText()));

                    System.out.println(purchase);

                    System.out.println(purchase.getDerivative().getParent().getStock().getOid());

                    */
                    /*
                    (.setStatus 1)
                    (.setDx ~dx)
                    (.setPrice ~price)
                    (.setBuyAtPurchase ~buy)
                    (.setVolume ~volume)
                    (.setPurchaseType ~ptype)
                    (.setSpotAtPurchase ~spot))
                    */


                    derivativeRepository.registerOptionPurchase(fx, cat.getValue(), purchaseAmount);
                }
            }
        });

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

    //endregion Public Methods
    public void addDerivativesControllerListener(DerivativesControllerListener listener) {
        if (calculatedListeners == null) {
            calculatedListeners = new ArrayList<>();
        }
        calculatedListeners.add(listener);
    }
    public void setStock(Stock stock) {
        if (stock == null) {
            log.warn("Stock was null");
            return;
        }

        this.stock = stock;

        _updateDerivatives();
    }

    public void updateSpot() {

    }

    public void updateDerivatives() {
        derivativeRepository.invalidate();
        _updateDerivatives();
    }

    private void _updateDerivatives() {
        String ticker = this.stock.getTicker();
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
        if (_selectedLoadStockProperty.get() == true) {
            StockPrice spot = derivativeRepository.spot(ticker);
            stockPrice.setPrice(spot);
            fireAssignStockPriceEvent(spot);
        }
    }

    //endregion Public Methods
    //region Private Methods
    private void fireAssignStockPriceEvent(StockPrice spot) {
        if (calculatedListeners != null) {
            calculatedListeners.stream().forEach(l -> l.notifySpotUpdated(spot));
        }
    }
    private void initStockPrice() {
        StringConverter<? extends Number> converter =  new DoubleStringConverter();
        Bindings.bindBidirectional(txSpot.textProperty(), stockPrice.clsProperty(), (StringConverter<Number>) converter);
        Bindings.bindBidirectional(txOpen.textProperty(), stockPrice.opnProperty(),  (StringConverter<Number>)converter);
        Bindings.bindBidirectional(txHi.textProperty(), stockPrice.hiProperty(),  (StringConverter<Number>)converter);
        Bindings.bindBidirectional(txLo.textProperty(), stockPrice.loProperty(),  (StringConverter<Number>)converter);
    }
    private void load(Function<String,Collection<DerivativeFx>> action,String ticker) {
        if  (_selectedLoadDerivativesProperty.get() == true) {
            ObservableList<DerivativeFx> items = FXCollections.observableArrayList(action.apply(ticker));
            derivativesTableView.getItems().setAll(items);
            //setChoiceBoxRiscValues(items);
        }
    }

    private List<RiscItem> riscItems(double low, double hi) {
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

    /*private void setChoiceBoxRiscValues(ObservableList<DerivativeFx> derivatives) {
        Comparator<DerivativeFx> compFx = (a,b) -> {
            double aVal = a.getSell();
            double bVal = b.getSell();
            if (aVal < bVal) {
                return -1;
            }
            else if (aVal > bVal) {
                return 1;
            }
            else {
                return 0;
            }
        };
        cbRisc.getItems().clear();

        Optional<DerivativeFx> maxFxObj = derivatives.stream().max(compFx);
        Optional<DerivativeFx> minFxObj = derivatives.stream().min(compFx);
        double maxFx = maxFxObj.isPresent() ? maxFxObj.get().getSell() : 100.0;
        double minFx = minFxObj.isPresent() ? minFxObj.get().getSell() : 0.0;
        System.out.println("Min: " + minFx + ", max: " + maxFx);

        final ObservableList<RiscItem> cbitems = FXCollections.observableArrayList(riscItems(maxFx,minFx));
        cbRisc.getItems().addAll(cbitems);
        cbRisc.getSelectionModel().selectedIndexProperty().addListener((observable,value,newValue) -> {
            calcRisc(cbitems.get(newValue.intValue()));
        });
    }*/
    private void initChoiceBoxRisc() {
        final ObservableList<RiscItem> cbitems = FXCollections.observableArrayList(riscItems());

        cbRisc.getItems().addAll(cbitems);
        cbRisc.getSelectionModel().selectedIndexProperty().addListener((observable, value, newValue) -> {
            calcRisc(cbitems.get(newValue.intValue()));
        });
        txRisc.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double newRiscValue = Double.parseDouble(newValue.replace(",","."));
                if (newRiscValue > 0) {
                    calcRisc(new RiscItem(newRiscValue));
                }
            }
            catch (Exception ex) {
            }
        });
    }
    private void initChoiceBoxPurchaseCategory(){
        List<PurchaseCategory> items = new ArrayList<>();
        items.add(new PurchaseCategory(3,"Real Trade"));
        items.add(new PurchaseCategory(4,"Test Trade"));
        items.add(new PurchaseCategory(11,"Paper Trade"));
        final ObservableList<PurchaseCategory> cbitems = FXCollections.observableArrayList(items);
        cbPurchaseType.getItems().addAll(cbitems);
    }
    private void calcRisc(RiscItem riscItem) {
        List<DerivativeFx> calculated = new ArrayList<>();

        for (DerivativeFx fx : derivativesTableView.getItems()) {
            if (fx.isCheckedProperty().get() == true) {
                fx.setRisk(riscItem.getValue());
                calculated.add(fx);
            }
        }

        if (calculated.size() > 0) {
            fireCalculatedEvent(calculated);
        }
    }

    private void fireCalculatedEvent(List<DerivativeFx> calculated) {
        /*if (log.isDebugEnabled()) {
            log.debug(String.format("fireCalculatedEvent listeners: %d",calculatedListeners.size()));
        }*/

        if (calculatedListeners.size() == 0) return;

        /*DerivativesCalculatedEvent evt = new DerivativesCalculatedEvent(calculated);
        for (DerivativesControllerListener l : calculatedListeners) {
            l.notify(evt);
        }*/
        for (DerivativesControllerListener l : calculatedListeners) {
            l.notifyDerivativesCalculated(calculated);
        }
    }
    //endregion Private Methods

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
