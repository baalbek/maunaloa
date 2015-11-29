package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

/**
 * Created by rcs on 4/12/14.
 *
 */
public class MainframeController {
    //region FXML
    @FXML private ChoiceBox cbTickers;
    @FXML private ChoiceBox cbShiftAmount;
    @FXML private ToggleGroup rgDerivatives;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;
    @FXML private CheckBox cxComments;
    @FXML private CheckBox cxIsCloud;
    @FXML private TabPane myTabPane;
    @FXML private Label lblLocalMongodbUrl;
    @FXML private Label lblSqlUrl;
    @FXML private Button btnShiftLeft;
    @FXML private Button btnShiftRight;
    @FXML private CheckMenuItem mnuShiftAllCharts;
    @FXML private CheckMenuItem mnuIsShiftDays;
    @FXML private BorderPane myBorderPane;

    //endregion FXML

    //region Initialize

    @SuppressWarnings("unchecked")
    public void initialize() {
        myBorderPane.setPrefSize(width,height);

        cbShiftAmount.getItems().add(1);
        cbShiftAmount.getItems().add(2);
        cbShiftAmount.getItems().add(3);
        cbShiftAmount.getItems().add(4);
        cbShiftAmount.getItems().add(5);
        cbShiftAmount.getItems().add(6);
        cbShiftAmount.getItems().add(7);
        cbShiftAmount.getItems().add(8);

        shiftAmountProperty.bind(cbShiftAmount.getSelectionModel().selectedItemProperty());

        shiftBothChartsProperty.bind(mnuShiftAllCharts.selectedProperty());

        isShiftDaysProperty.bind(mnuIsShiftDays.selectedProperty());

    }

    //endregion Initialize

    //region Properties
    private IntegerProperty shiftAmountProperty = new SimpleIntegerProperty(6);
    private BooleanProperty shiftBothChartsProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isShiftDaysProperty = new SimpleBooleanProperty(true);
    private double width = 1400.0;
    private double height = 850.0;

    //endregion Properties
}

