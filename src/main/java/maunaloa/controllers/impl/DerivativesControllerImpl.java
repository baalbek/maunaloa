package maunaloa.controllers.impl;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import maunaloa.beans.CalculatedDerivativeBean;
import maunaloa.controllers.DerivativesController;
import maunaloa.utils.DateUtils;
import oahu.controllers.ChartViewModel;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
import oahu.models.MaunaloaFacade;
import oahu.views.MaunaloaChart;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DerivativesControllerImpl implements DerivativesController, ChartViewModel {

    //region Init
    @FXML private TableView<DerivativeBean> derivativesTableView;

    @FXML private TableColumn<DerivativeBean, String> colOpName;
    @FXML private TableColumn<DerivativeBean, Boolean> colSelected;
    @FXML private TableColumn<DerivativeBean, Date> colExpiry;

    @FXML private TableColumn<CalculatedDerivativeBean, Double> colBuy;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colIvBuy;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colIvSell;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colSpread;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colDelta;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colDays;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colRisc;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colSpRisc;

    @FXML private ToggleGroup rgDerivatives;

    @FXML private ChoiceBox cbTickers;
    @FXML private VBox vboxCandlesticks;
    @FXML private Canvas myCanvas;
    @FXML private CheckBox cxLoadOptionsHtml;

    @FXML private TextField txSpot;

    private MaunaloaChart chart;

    private ObservableList<DerivativeBean> beans;
    private String ticker = null;
    private List<String> tickers;

    //private StockBean stock;

    private MaunaloaFacade facade;

    private Date defaultStartDate = DateUtils.createDate(2012,1,1);

    public DerivativesControllerImpl() {
    }

    public DerivativesControllerImpl(MaunaloaFacade facade) {
        this.facade = facade;
    }


    public void initialize() {
        initChoiceBoxTickers();
        initGrid();
        initMyCanvas();

        cxLoadOptionsHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadOptionsHtml.isSelected()) {
                    System.out.println("yep " + cbTickers.valueProperty().get());
                    ObservableList<DerivativeBean> items = derivatives();
                    if (items != null) {
                        derivativesTableView.getItems().setAll(items);
                    }
                }
            }
        });

        rgDerivatives.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue,
                                                          Toggle toggle,
                                                          Toggle toggle2) {
                String ticker = cbTickers.valueProperty().get().toString();
                String opType = observableValue.getValue().getUserData().toString();
                ObservableList<DerivativeBean> items = fetchDerivativesForTicker(ticker,opType);
                derivativesTableView.getItems().setAll(items);
            }
        });

        txSpot.textProperty().bind(spotProperty());

        /*
        txRisk.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                        System.out.println("textField old val: " + arg1);
                        System.out.println("textField new val: " + arg2);
                        System.out.println();
                    }
                });
         */

        /*
        final DoubleProperty a = new SimpleDoubleProperty(1);
        final DoubleProperty b = new SimpleDoubleProperty(2);
        final DoubleProperty c = new SimpleDoubleProperty(3);
        final DoubleProperty d = new SimpleDoubleProperty(4);

        DoubleBinding db = new DoubleBinding() {

            {
                super.bind(a, b, c, d);
            }

            @Override
            protected double computeValue() {
                return (a.get() * b.get()) + (c.get() * d.get());
            }
        };
        */

    }
    //endregion Init

    //region Public Methods


    public void calcRisk(ActionEvent event) {
        String txVal = ((TextField)event.getSource()).textProperty().get();
        double risk = Double.parseDouble(txVal);

        for (DerivativeBean b : derivativesTableView.getItems()) {
            CalculatedDerivativeBean cb = (CalculatedDerivativeBean)b;
            if (cb.getIsChecked()) {
                cb.setRisk(risk);
            }
        }
    }

    public void unCheckBeans(ActionEvent event) {
        for (DerivativeBean b : derivatives()) {
            b.setIsChecked(false);
        }
    }



    public ObservableList<DerivativeBean> derivatives() {
        if (ticker == null) return null;

        String userData = rgDerivatives.getSelectedToggle().getUserData().toString();

        return fetchDerivativesForTicker(ticker,userData);
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
        if (cxLoadOptionsHtml.isSelected()) {
            ObservableList<DerivativeBean> items = derivatives();
            if (items != null) {
                derivativesTableView.getItems().setAll(items);
            }
        }
        StockBean stock = facade.spot(ticker);
        spotProperty().set(String.valueOf(stock.getValue()));
        draw();
    }

    public void selectAllDerivatives(ActionEvent event)  {
        toggleAllDerivatives(true);
    }

    public void unSelectAllDerivatives(ActionEvent event)  {
        toggleAllDerivatives(false);
    }
    //endregion  Public Methods

    //region Private Methods
    private ObservableList<DerivativeBean> fetchDerivativesForTicker(String ticker, String optionType) {
        Collection<DerivativeBean> result = null;
        switch (optionType) {
            case "calls": {
                result = facade.calls(ticker);
                break;
            }
            case "puts": {
                result = facade.puts(ticker);
                break;
            }
            default: {
                result = facade.callsAndPuts(ticker);
            }
        }
        return FXCollections.observableArrayList(result);
    }
    private void toggleAllDerivatives(boolean value) {
        for (DerivativeBean b : derivatives()) {
            b.setIsChecked(value);
        }
    }
    //endregion Private Methods

    //region Interface methods

    @Override
    public void draw() {
        if (ticker == null) return;
        chart.draw(myCanvas);
    }

    @Override
    public Collection<StockBean> stockPrices(int period) {
        return facade.stockPrices(ticker, defaultStartDate, period);
    }
    //endregion  Interface methods

    //region Properties

    private StringProperty spot = new SimpleStringProperty("0.0");
    private StringProperty spotProperty()  {
        return spot;
    }

    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
        this.chart.setViewModel(this);
    }

    public List<String> getTickers() {
        return tickers;
    }

    public void setTickers(List<String> tickers) {
        this.tickers = tickers;
    }
    //endregion

    //region Initialization methods
    private void initChoiceBoxTickers() {
        final ObservableList<String> cbitems = FXCollections.observableArrayList();
        for (String s : getTickers()) {
            cbitems.add(s);
        }
        cbTickers.getItems().addAll(cbitems);
        cbTickers.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                        setTicker(cbitems.get(newValue.intValue()));
                    }
                }
        );
    }

    private void initGrid() {
        colOpName.setCellValueFactory(new PropertyValueFactory<DerivativeBean, String>("ticker"));
        colSelected.setCellValueFactory(new PropertyValueFactory<DerivativeBean, Boolean>("isChecked"));
        colSelected.setCellFactory(
                new Callback<TableColumn<DerivativeBean, Boolean>, TableCell<DerivativeBean, Boolean>>() {
                    @Override
                    public TableCell<DerivativeBean, Boolean> call(TableColumn<DerivativeBean, Boolean> p) {
                        return new CheckBoxTableCell<>();
                    }
                });
        colExpiry.setCellValueFactory(new PropertyValueFactory<DerivativeBean, Date>("expiry"));

        colBuy.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("buy"));

        colIvBuy.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("ivBuy"));
        colIvSell.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("ivSell"));
        colDelta.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("delta"));
        colSpread.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("spread"));
        colDays.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("days"));
        colRisc.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("risk"));
        colSpRisc.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("stockPriceRisk"));
    }

    private void initMyCanvas() {
        myCanvas.widthProperty().bind(vboxCandlesticks.widthProperty());
        myCanvas.heightProperty().bind(vboxCandlesticks.heightProperty());

        InvalidationListener listener =     new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                draw();
            }
        };

        myCanvas.widthProperty().addListener(listener);
        myCanvas.heightProperty().addListener(listener);
    }
    //endregion  Initialization methods
}
