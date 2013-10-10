package maunaloa.controllers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.controllers.DerivativesController;
import maunaloa.controllers.MainFrameController;
import maunaloa.events.FibonacciEvent;
import maunaloa.events.MainFrameControllerListener;
import maunaloa.models.MaunaloaFacade;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahux.chart.MaunaloaChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DefaultMainFrameController implements MainFrameController {
    //region FXML
    @FXML private ChartCanvasController obxCandlesticksController;
    @FXML private ChartCanvasController obxWeeksController;
    @FXML private ChartCanvasController candlesticksController;
    @FXML private ChartCanvasController weeksController;
    @FXML private DerivativesController optionsController;
    @FXML private ChoiceBox cbTickers;
    @FXML private MenuBar myMenuBar;
    @FXML private Menu fibonacciMenu;
    @FXML private Menu mongodbMenu;
    @FXML private ToggleGroup rgDerivatives;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;
    @FXML private TabPane myTabPane;
    //endregion FXML

    //region Init

    private MaunaloaFacade facade;
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;
    private MaunaloaChart obxWeeklyChart;
    private MaunaloaChart obxCandlesticksChart;
    private CheckMenuItem fib1272extCheckMenu;

    private List<MainFrameControllerListener> myListeners = new ArrayList<>();

    public DefaultMainFrameController() {
    }


    //endregion Init

    //region FXML Actions

    public void close(ActionEvent event)  {
        System.exit(0);
    }

    //endregion

    //region Initialization methods

    public List<Stock> getTickers() {
        return facade.getTickers();
    }

    public void setTicker(Stock ticker) {

        switch (ticker.getTickerCategory()) {
            case 1:
                candlesticksController.setTicker(ticker);
                weeksController.setTicker(ticker);
                optionsController.setTicker(ticker);
                break;
            case 2:
                obxCandlesticksController.setTicker(ticker);
                obxWeeksController.setTicker(ticker);
                break;
        }
        /*
        if (cxLoadStockHtml.isSelected()) {
            stock.assign(facade.spot(ticker));
        }
        draw();
        */
    }

    private void initChoiceBoxTickers() {
        final ObservableList<Stock> cbitems = FXCollections.observableArrayList(getTickers());
        cbTickers.setConverter(new StringConverter<Stock>() {
            @Override
            public String toString(Stock o) {
                return String.format("[%s] %s",o.getTicker(),o.getCompanyName());
            }

            @Override
            public Stock fromString(String s) {
                throw new NotImplementedException();
            }
        });
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

    private void notifyOptionsController() {
        Object prop = cbTickers.valueProperty().get();
        if (prop == null) return;
        Stock ticker = (Stock)prop;
        optionsController.setTicker(ticker);
    }

    private void initOptionsController() {
        rgDerivatives.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue,
                                Toggle toggle,
                                Toggle toggle2) {
                notifyOptionsController();
            }
        });

        cxLoadOptionsHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadOptionsHtml.isSelected()) {
                    notifyOptionsController();
                }
            }
        });

        cxLoadStockHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadStockHtml.isSelected()) {
                    notifyOptionsController();
                }
            }
        });

        optionsController.selectedDerivativeProperty().bind(rgDerivatives.selectedToggleProperty());
        optionsController.selectedLoadStockProperty().bind(cxLoadStockHtml.selectedProperty());
        optionsController.selectedLoadDerivativesProperty().bind(cxLoadOptionsHtml.selectedProperty());
        optionsController.setModel(getFacade());
        optionsController.addDerivativesControllerListener(candlesticksController);
        optionsController.addDerivativesControllerListener(weeksController);
    }

    public void initialize() {

        initChoiceBoxTickers();
        initMenus();


        initCanvanController(candlesticksController,"Candlesticks",1,getCandlesticksChart());
        initCanvanController(weeksController,"Weeks",2,getWeeklyChart());
        initCanvanController(obxCandlesticksController,"OBX Candlest.",3,getObxCandlesticksChart());
        initCanvanController(obxWeeksController, "OBX Weeks", 4, getObxWeeklyChart());

        initOptionsController();
    }

    private void initMenus() {
        fib1272extCheckMenu = new CheckMenuItem("1.272 extension");
        fib1272extCheckMenu.setSelected(false);
        fibonacciMenu.getItems().addAll(fib1272extCheckMenu, new SeparatorMenuItem());

        MenuItem m1 = new MenuItem("New Line");
        m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int curloc = myTabPane.getSelectionModel().getSelectedIndex();
                for (MainFrameControllerListener listener : myListeners) {
                    listener.onFibonacciEvent(new FibonacciEvent(curloc,FibonacciEvent.NEW_LINE));
                }
            }
        });
        MenuItem m2 = new MenuItem("Hide selected Lines");
        m2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int curloc = myTabPane.getSelectionModel().getSelectedIndex();
                for (MainFrameControllerListener listener : myListeners) {
                    listener.onFibonacciEvent(new FibonacciEvent(curloc,FibonacciEvent.HODE_SEL_LINES));
                }
            }
        });
        MenuItem m3 = new MenuItem("Hide all Lines");
        m3.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int curloc = myTabPane.getSelectionModel().getSelectedIndex();
                for (MainFrameControllerListener listener : myListeners) {
                    listener.onFibonacciEvent(new FibonacciEvent(curloc,FibonacciEvent.HODE_ALL_LINES));
                }
            }
        });
        MenuItem m4 = new MenuItem("Delete selected Lines");
        m4.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int curloc = myTabPane.getSelectionModel().getSelectedIndex();
                for (MainFrameControllerListener listener : myListeners) {
                    listener.onFibonacciEvent(new FibonacciEvent(curloc,FibonacciEvent.DELETE_SEL_LINES));
                }
            }
        });
        MenuItem m5 = new MenuItem("Delete all Lines");
        m5.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int curloc = myTabPane.getSelectionModel().getSelectedIndex();
                for (MainFrameControllerListener listener : myListeners) {
                    listener.onFibonacciEvent(new FibonacciEvent(curloc,FibonacciEvent.DELETE_ALL_LINES));
                }
            }
        });
        fibonacciMenu.getItems().addAll(m1,
                                        new SeparatorMenuItem(),
                                        m2,m3,
                                        new SeparatorMenuItem(),
                                        m4,m5);

    }
    private void initCanvanController(ChartCanvasController controller,
                                      String name,
                                      int location,
                                      MaunaloaChart chart) {

        controller.setName(name);
        controller.setLocation(location);
        controller.setChart(chart);
        controller.setModel(getFacade());
        controller.fibonacci1272extProperty().bind(fib1272extCheckMenu.selectedProperty());

        myListeners.add(controller);

    }

    //endregion  Initialization methods

    //region Properties

    public MaunaloaChart getCandlesticksChart() {
        return candlesticksChart;
    }

    public void setCandlesticksChart(MaunaloaChart candlesticksChart) {
        this.candlesticksChart = candlesticksChart;
    }

    public MaunaloaChart getWeeklyChart() {
        return weeklyChart;
    }

    public void setWeeklyChart(MaunaloaChart weeklyChart) {
        this.weeklyChart = weeklyChart;
    }


    public MaunaloaFacade getFacade() {
        return facade;
    }

    public void setFacade(MaunaloaFacade facade) {
        this.facade = facade;
    }

    public MaunaloaChart getObxWeeklyChart() {
        return obxWeeklyChart;
    }

    public void setObxWeeklyChart(MaunaloaChart obxWeeklyChart) {
        this.obxWeeklyChart = obxWeeklyChart;
    }

    public MaunaloaChart getObxCandlesticksChart() {
        return obxCandlesticksChart;
    }

    public void setObxCandlesticksChart(MaunaloaChart obxCandlesticksChart) {
        this.obxCandlesticksChart = obxCandlesticksChart;
    }

    //endregion

    //region Interface Methods
    //endregion
}
