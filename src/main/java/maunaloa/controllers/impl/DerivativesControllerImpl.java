package maunaloa.controllers.impl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import maunaloa.controllers.DerivativesController;
import maunaloa.domain.RiscItem;
import maunaloa.events.DerivativesCalculatedEvent;
import maunaloa.events.DerivativesCalculatedListener;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahux.domain.DerivativeFx;
import oahux.models.MaunaloaFacade;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DerivativesControllerImpl implements DerivativesController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

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

    @FXML private ChoiceBox cbRisc;
    @FXML private TextField txSpot;
    @FXML private TextField txOpen;
    @FXML private TextField txHi;
    @FXML private TextField txLo;
    private MaunaloaFacade model;
    private MenuBar menuBar;
    private List<DerivativesCalculatedListener> calculatedListeners;
    //endregion FXML

    //region Init

    public DerivativesControllerImpl() {
        calculatedListeners = new ArrayList<>();
    }
    //endregion Init

    //region FXML Actions

    /*
    public void calcRisk(ActionEvent event) {
        String txVal = ((TextField)event.getSource()).textProperty().get();
        double risk = Double.parseDouble(txVal);

        List<DerivativeFx> calculated = new ArrayList<>();

        for (DerivativeFx fx : derivativesTableView.getItems()) {
            if (fx.isCheckedProperty().get() == true) {
                fx.setRisk(risk);
                calculated.add(fx);
            }
        }

        if (calculated.size() > 0) {
            fireCalculatedEvent(calculated);
        }
    }
    //*/

    public void calcRisc(RiscItem item) {
        List<DerivativeFx> calculated = new ArrayList<>();

        for (DerivativeFx fx : derivativesTableView.getItems()) {
            if (fx.isCheckedProperty().get() == true) {
                fx.setRisk(item.getValue());
                calculated.add(fx);
            }
        }

        if (calculated.size() > 0) {
            fireCalculatedEvent(calculated);
        }
    }

    private void fireCalculatedEvent(List<DerivativeFx> calculated) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("fireCalculatedEvent listeners: %d",calculatedListeners.size()));
        }
        if (calculatedListeners.size() == 0) return;

        DerivativesCalculatedEvent evt = new DerivativesCalculatedEvent(calculated);
        for (DerivativesCalculatedListener l : calculatedListeners) {
            l.notify(evt);
        }
    }

    //endregion

    //region Private Methods

    private void loadCalls(String ticker) {
        if  (_selectedLoadDerivativesProperty.get() == true) {
            derivativesTableView.getItems().setAll(FXCollections.observableArrayList(model.calls(ticker)));
        }
        else if (_selectedLoadStockProperty.get() == true) {
        }
    }
    private void loadPuts(String ticker) {
        if (ticker == null) return;
        if  (_selectedLoadDerivativesProperty.get() == true) {
            derivativesTableView.getItems().setAll(FXCollections.observableArrayList(model.puts(ticker)));
        }
        else if (_selectedLoadStockProperty.get() == true) {
        }
    }
    private void loadAll() {

    }

    //endregion Private Methods

    //region Initialization methods
    public void initialize() {
        initChoiceBoxRisc();
        initGrid();
    }

    private List<RiscItem> getRiscItems() {
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
    private void initChoiceBoxRisc() {
        final ObservableList<RiscItem> cbitems = FXCollections.observableArrayList(getRiscItems());
        cbRisc.setConverter(new StringConverter<RiscItem>() {
            @Override
            public String toString(RiscItem o) {
                return String.format(" %s", o.getValue());
            }

            @Override
            public RiscItem fromString(String s) {
                throw new NotImplementedException();
            }
        });
        cbRisc.getItems().addAll(cbitems);
        cbRisc.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                    calcRisc(cbitems.get(newValue.intValue()));
                }
            }
        );

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
    public void setTicker(Stock ticker) {
        switch (_selectedDerivativeProperty.get().getUserData().toString()) {
            case "calls":
                loadCalls(ticker.getTicker());
                break;
            case "puts":
                loadPuts(ticker.getTicker());
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

    @Override
    public void addDerivativesCalculatedListener(DerivativesCalculatedListener listener) {
        calculatedListeners.add(listener);
    }
    //endregion  Interface methods

    //region Properties

    //endregion
}
