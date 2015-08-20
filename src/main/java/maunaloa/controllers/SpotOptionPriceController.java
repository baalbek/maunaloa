package maunaloa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ranoraraku.beans.options.SpotOptionPriceBean;

/**
 * Created by rcs on 19.08.15.
 *
 */
public class SpotOptionPriceController {
    @FXML private TableView<SpotOptionPriceBean> spotOptionPriceTableView;
    @FXML private TableColumn<SpotOptionPriceBean, String> colOpxId;
    @FXML private TableColumn<SpotOptionPriceBean, String> colOpxName;
    @FXML private TableColumn<SpotOptionPriceBean, String> colExpiry;
    @FXML private TableColumn<SpotOptionPriceBean, String> colDays;
    @FXML private TableColumn<SpotOptionPriceBean, String> colBuy;
    @FXML private TableColumn<SpotOptionPriceBean, String> colSell;

    //region Initialization methods
    public void initialize() {
        initGrid();
    }

    private void initGrid() {

    }
    //endregion Initialization methods

}
