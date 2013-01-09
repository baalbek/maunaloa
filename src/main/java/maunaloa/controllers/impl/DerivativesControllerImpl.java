package maunaloa.controllers.impl;

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

    @FXML private ToggleGroup rgDerivatives;


    @FXML private ChoiceBox cbTickers;
    @FXML private Canvas myCanvas;

    private MaunaloaChart chart;

    private ObservableList<DerivativeBean> beans;
    private String ticker = null;
    private List<String> tickers;

    private MaunaloaFacade facade;

    private Date defaultStartDate = DateUtils.createDate(2012,1,1);

    public DerivativesControllerImpl() {
    }

    public DerivativesControllerImpl(MaunaloaFacade facade) {
        this.facade = facade;
    }

    public void initialize() {
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

        //System.out.println(rgDerivatives.getSelectedToggle().getUserData());

        //derivativesTableView.getItems().setAll(derivatives());

        /*
        myCanvas.widthProperty().bind(myVbox.widthProperty());
        myCanvas.heightProperty().bind(myVbox.heightProperty());
                InvalidationListener listener =     new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                draw();
            }
        };

        myCanvas.widthProperty().addListener(listener);
        myCanvas.heightProperty().addListener(listener);
        */

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

    public void editBean(ActionEvent event) {
        DerivativeBean bean = beans.get(0);
        bean.tickerProperty().set("Changed!");
        bean.setTicker("Changed again!!!!!");
        System.out.println("Changed bean: " + bean);

        for (DerivativeBean b : derivatives()) {
            System.out.println(b.tickerProperty().get() + " is checked: " + b.isCheckedProperty().get());
        }

        for (StockBean b : stockPrices(1)) {
            System.out.println(b.getTicker() + " " + b.getCls());
        }

    }
    public void unCheckBeans(ActionEvent event) {
        for (DerivativeBean b : derivatives()) {
            b.setIsChecked(false);
        }
    }
    /*
    public ObjectProperty<ObservableList<DerivativeBean>> derivativesProperty() {

        if (ticker == null) return null;
        return FXCollections.observableArrayList(facade.calls(ticker));
    }
    */

    public ObservableList<DerivativeBean> derivatives() {
        if (ticker == null) return null;

        Collection<DerivativeBean> result = null;

        String userData = rgDerivatives.getSelectedToggle().getUserData().toString();

        System.out.println("User data: " + userData);
        switch (userData) {
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




    public void setTicker(String ticker) {
        this.ticker = ticker;
        ObservableList<DerivativeBean> items = derivatives();
        if (items != null) {
            derivativesTableView.getItems().setAll(items);
        }
        draw();
    }

    //--------------------------------------------------------------
    //----------------- Interface methods --------------------------
    //--------------------------------------------------------------

    @Override
    public void draw() {
        if (ticker == null) return;
        chart.draw(myCanvas);
    }

    @Override
    public Collection<StockBean> stockPrices(int period) {
        return facade.stockPrices(ticker, defaultStartDate, period);
    }

    //--------------------------------------------------------------
    //------------------------ Properties --------------------------
    //--------------------------------------------------------------
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
}
