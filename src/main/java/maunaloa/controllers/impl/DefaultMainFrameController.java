package maunaloa.controllers.impl;

import com.mongodb.DBObject;
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
import maunaloa.domain.MaunaloaContext;
import maunaloa.events.FibonacciEvent;
import maunaloa.events.MainFrameControllerListener;
import maunaloa.events.mongodb.FetchFromMongoDBEvent;
import maunaloa.events.mongodb.SaveToMongoDBEvent;
import maunaloax.models.ChartWindowDressingModel;
import maunaloa.models.MaunaloaFacade;
import maunaloa.utils.FxUtils;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahux.chart.IDateBoundaryRuler;
import oahux.chart.MaunaloaChart;
import org.apache.log4j.Logger;

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
    @FXML private Menu levelsMenu;
    @FXML private Menu mongodbMenu;
    @FXML private ToggleGroup rgDerivatives;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;
    @FXML private TabPane myTabPane;
    //endregion FXML

    //region Init

    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private MaunaloaFacade facade;
    private ChartWindowDressingModel windowDressingModel;
    private MaunaloaChart candlesticksChart;
    private MaunaloaChart weeklyChart;
    private MaunaloaChart obxWeeklyChart;
    private MaunaloaChart obxCandlesticksChart;
    private CheckMenuItem fib1272extCheckMenu;

    private List<MainFrameControllerListener> myListeners = new ArrayList<>();
    private Stock currentTicker;

    public final static int SAVE_TO_DATASTORE  = 1;
    public final static int FETCH_FROM_DATASTORE  = 2;
    public final static int COMMENTS  = 3;

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

        this.currentTicker = ticker;
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

        MenuItem m1 = createFibonacciMenuItem("New Line", FibonacciEvent.NEW_LINE);
        MenuItem m2 = createFibonacciMenuItem("Hide selected Lines", FibonacciEvent.HODE_SEL_LINES);
        MenuItem m3 = createFibonacciMenuItem("Hide all Lines", FibonacciEvent.HODE_ALL_LINES);
        MenuItem m4 = createFibonacciMenuItem("Delete selected Lines", FibonacciEvent.DELETE_SEL_LINES);
        MenuItem m5 = createFibonacciMenuItem("Delete all Lines", FibonacciEvent.DELETE_ALL_LINES);
        fibonacciMenu.getItems().addAll(m1,
                                        new SeparatorMenuItem(),
                                        m2,m3,
                                        new SeparatorMenuItem(),
                                        m4,m5);


        MenuItem mongo1a = createMongoDBMenuItem("Save to datastore", SAVE_TO_DATASTORE);
        MenuItem mongo2a = createMongoDBMenuItem("Fetch from datastore", FETCH_FROM_DATASTORE);
        MenuItem mongo3a = createMongoDBMenuItem("Comments", COMMENTS);
        mongodbMenu.getItems().addAll(
                mongo2a,
                new SeparatorMenuItem(),
                mongo3a,
                new SeparatorMenuItem(),
                mongo1a);

        MenuItem levels1 = createLevelsMenuItem("New Level");
        levelsMenu.getItems().add(levels1);
    }

    private MenuItem createLevelsMenuItem(final String title) {
        MenuItem m = new MenuItem(title);

        m.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int curloc = myTabPane.getSelectionModel().getSelectedIndex();

                MaunaloaContext ctx = new MaunaloaContext();

                ctx.setLocation(curloc);

                ctx.setListeners(myListeners);

                FxUtils.loadApp(ctx,"/NewLevelDialog.fxml", title);
            }
        });

        return m;
    }
    private MenuItem createMongoDBMenuItem(String title,
                                           final int mongoEventId) {
        MenuItem m = new MenuItem(title);
        m.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int curloc = myTabPane.getSelectionModel().getSelectedIndex();

                switch (mongoEventId) {
                    case SAVE_TO_DATASTORE:
                        SaveToMongoDBEvent event = new SaveToMongoDBEvent(curloc);
                        for (MainFrameControllerListener listener : myListeners) {
                            listener.onSaveToMongoDBEvent(event);
                        }
                        break;
                    case FETCH_FROM_DATASTORE:
                        MainFrameControllerListener curListener = findListener(curloc);
                        List<DBObject> lines = getFacade().getWindowDressingModel().fetchFibonacci(
                                currentTicker.getTicker(),
                                curloc,
                                null, // dbr.getStartDate(),
                                null); // dbr.getEndDate());

                        log.info(String.format("Fetching from mongodb lines for ticker: %s, location: %d, num lines: %d",
                                currentTicker.getTicker(),
                                curloc,
                                lines == null ? 0 : lines.size()));
                        curListener.onFetchFromMongoDBEvent(new FetchFromMongoDBEvent(lines));

                        break;
                    case COMMENTS:
                        MaunaloaContext ctx2 = new MaunaloaContext();
                        ctx2.setWindowDressingModel(getWindowDressingModel());
                        ChartCanvasController ccc2 = (ChartCanvasController)findListener(curloc);

                        ctx2.setLines(ccc2.getLines());

                        FxUtils.loadApp(ctx2,"/ChartCommentsDialog.fxml","MongoDB Comments");
                        break;
                }
            }
        });
        return m;
    }

    private MainFrameControllerListener findListener(int location) {
        MainFrameControllerListener result = null;
        for (MainFrameControllerListener listener : myListeners) {
            ChartCanvasController ccc = (ChartCanvasController)listener;
            if (ccc.getLocation() == location) {
                result = listener;
                break;
            }
        }
        return result;
    }

    private MenuItem createFibonacciMenuItem(String title,
                                             final int fibEvent) {
        MenuItem m = new MenuItem(title);
        m.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int curloc = myTabPane.getSelectionModel().getSelectedIndex();
                FibonacciEvent evt = new FibonacciEvent(curloc,fibEvent);
                for (MainFrameControllerListener listener : myListeners) {
                    listener.onFibonacciEvent(evt);
                }
            }
        });
        return m;
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

    public ChartWindowDressingModel getWindowDressingModel() {
        return windowDressingModel;
    }

    public void setWindowDressingModel(ChartWindowDressingModel windowDressingModel) {
        this.windowDressingModel = windowDressingModel;
    }

    //endregion

    //region Interface Methods
    //endregion
}
