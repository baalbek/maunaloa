package maunaloa.controllers.impl;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import maunaloa.controllers.DerivativesController;
import maunaloa.utils.DateUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import oahu.controllers.ChartViewModel;
import oahu.financial.beans.DerivativeBean;
import oahu.financial.beans.StockBean;
import oahu.models.MaunaloaFacade;
import oahu.views.MaunaloaChart;

import java.util.ArrayList;
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

    @FXML private ChoiceBox cbTickers;
    @FXML private Canvas myCanvas;

    private MaunaloaChart chart;

    private ObservableList<DerivativeBean> beans;
    private String ticker = null;

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

        //derivativesTableView.getItems().setAll(derivatives());

        /*
        derivativesTableView.itemsProperty().bind(new ObjectProperty<ObservableList<DerivativeBean>>() {
            @Override
            public ObservableList<DerivativeBean> get() {
                return derivatives();
            }


            @Override
            public void bind(ObservableValue<? extends ObservableList<DerivativeBean>> observableValue) {
                //throw new org.apache.commons.lang.NotImplementedException();
            }

            @Override
            public void unbind() {
                //throw new org.apache.commons.lang.NotImplementedException();
            }

            @Override
            public boolean isBound() {
                //return false;throw new org.apache.commons.lang.NotImplementedException();
                return true;
            }

            @Override
            public Object getBean() {
                //return null;throw new org.apache.commons.lang.NotImplementedException();
                return null;
            }

            @Override
            public String getName() {
                //return null;throw new org.apache.commons.lang.NotImplementedException();
                return null;
            }

            @Override
            public void addListener(ChangeListener<? super ObservableList<DerivativeBean>> changeListener) {
                //throw new org.apache.commons.lang.NotImplementedException();
            }

            @Override
            public void removeListener(ChangeListener<? super ObservableList<DerivativeBean>> changeListener) {
                //throw new org.apache.commons.lang.NotImplementedException();
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {
                //throw new org.apache.commons.lang.NotImplementedException();
            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {
                //throw new org.apache.commons.lang.NotImplementedException();
            }

            @Override
            public void set(ObservableList<DerivativeBean> derivativeBeans) {
                //throw new org.apache.commons.lang.NotImplementedException();
            }
        });
        */
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
        cbitems.add("STL");
        cbitems.add("NHY");
        cbitems.add("YAR");
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
        /*
        if (beans == null) {
            ticker = "YAR";
            beans = FXCollections.observableArrayList(facade.calls(ticker));
        }
        */
        if (ticker == null) return null;
        return FXCollections.observableArrayList(facade.calls(ticker));
    }

    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
        this.chart.setViewModel(this);
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
    public List<StockBean> stockPrices(int period) {
        return facade.stockPrices(ticker, defaultStartDate, period);
    }
}
