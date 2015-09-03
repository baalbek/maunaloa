package maunaloa.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import oahu.financial.SpotOptionPrice;
import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;
import ranoraraku.beans.options.SpotOptionPriceBean;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Created by rcs on 19.08.15.
 *
 */
public class SpotOptionPriceController implements DerivativesControllerListener {
    @FXML private TableView<SpotOptionPrice> tableView;
    @FXML private TableColumn<SpotOptionPrice, Integer> colOpxId;
    @FXML private TableColumn<SpotOptionPrice, String> colOpxName;
    @FXML private TableColumn<SpotOptionPrice, Integer> colDays;
    @FXML private TableColumn<SpotOptionPrice, LocalDate> colPurchaseDate;
    @FXML private TableColumn<SpotOptionPrice, Double> colSpot;
    @FXML private TableColumn<SpotOptionPrice, Double> colBuy;
    @FXML private TableColumn<SpotOptionPrice, Double> colSell;
    private ControllerHub hub;

    //region Initialization methods
    public void initialize() {
        initGrid();
    }

    private void initGrid() {
        colOpxId.setCellValueFactory(new PropertyValueFactory<SpotOptionPrice, Integer>("opxId"));
        colOpxName.setCellValueFactory(new PropertyValueFactory<SpotOptionPrice, String>("opxName"));
        colDays.setCellValueFactory(new PropertyValueFactory<SpotOptionPrice, Integer>("days"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<SpotOptionPrice, LocalDate>("purchaseDate"));
        colSpot.setCellValueFactory(new PropertyValueFactory<SpotOptionPrice, Double>("spot"));
        colBuy.setCellValueFactory(new PropertyValueFactory<SpotOptionPrice, Double>("buy"));
        colSell.setCellValueFactory(new PropertyValueFactory<SpotOptionPrice, Double>("sell"));

    }
    //endregion Initialization methods

    public void setHub(ControllerHub hub) {
        this.hub = hub;
    }

    //region Interface DerivativesControllerListener
    @Override
    public void notifyDerivativesCalculated(List<DerivativeFx> calculated) {

    }

    @Override
    public void notifySpotUpdated(StockPrice spot) {

    }

    @Override
    public void notifySpotOptsEvent(List<DerivativeFx> items) {
        for (DerivativeFx item : items) {
            int opx_id = item.getDerivative().getOid();
            System.out.printf("Finding prices for %d\n", opx_id);
            System.out.printf("Item %s\n",item.getDerivative().getTicker());
            Collection<SpotOptionPrice> prices = hub.getStockRepository().findOptionPrices(opx_id);
            for (SpotOptionPrice price : prices) {
                System.out.printf("Opx id: %d\n", price.getOpxId());
            }
            ObservableList<SpotOptionPrice> itemsx = FXCollections.observableArrayList(prices);
            tableView.getItems().setAll(itemsx);
        }
    }
    //endregion Interface DerivativesControllerListener

}
