package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import maunaloa.repository.DerivativeRepository;
import oahu.financial.Derivative;
import oahu.financial.Stock;
import oahux.financial.DerivativeFx;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by rcs on 4/13/14.
 */
public class DerivativesController {
    //region FXML
    @FXML
    private TableView<DerivativeFx> derivativesTableView;
    //endregion FXML

    private DerivativeRepository derivativeRepository;
    private ObjectProperty<Toggle> _selectedDerivativeProperty = new SimpleObjectProperty<Toggle>();
    private BooleanProperty _selectedLoadStockProperty = new SimpleBooleanProperty();
    private BooleanProperty _selectedLoadDerivativesProperty = new SimpleBooleanProperty();

    public void setStock(Stock stock) {
        if (stock == null) return;

        /*
        Consumer<String> loadCalls = (String ticker) -> {

        };
        */

        switch (_selectedDerivativeProperty.get().getUserData().toString()) {
            case "calls":
                load((s) -> { return derivativeRepository.calls(s); }, stock.getTicker());
                break;
            case "puts":
                load((s) -> { return derivativeRepository.puts(s); }, stock.getTicker());
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
