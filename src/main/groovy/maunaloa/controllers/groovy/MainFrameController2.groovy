/**
 * Created by rcs on 1/4/14.
 */
package maunaloa.controllers.groovy

import com.mongodb.DBObject
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.control.TabPane
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.util.StringConverter
import maunaloa.controllers.ChartCanvasController
import maunaloa.controllers.DerivativesController
import maunaloa.controllers.MainFrameController
import maunaloa.domain.MaunaloaContext
import maunaloa.events.ChartCanvasLineEvent
import maunaloa.events.MainFrameControllerListener
import maunaloa.events.mongodb.FetchFromMongoDBEvent
import maunaloa.events.mongodb.SaveToMongoDBEvent
import maunaloa.models.MaunaloaFacade
import maunaloa.utils.FxUtils
import maunaloax.domain.ChartWindowsDressingContext
import maunaloax.models.ChartWindowDressingModel
import oahu.exceptions.NotImplementedException
import oahu.financial.Stock
import oahux.chart.MaunaloaChart
import org.apache.log4j.Logger
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationEventPublisher

class MainFrameController2  implements MainFrameController {
    //region FXML
    @FXML private ChartCanvasController obxCandlesticksController
    @FXML private ChartCanvasController obxWeeksController
    @FXML private ChartCanvasController candlesticksController
    @FXML private ChartCanvasController weeksController
    @FXML private DerivativesController optionsController
    @FXML private ChoiceBox cbTickers
    @FXML private MenuBar myMenuBar
    @FXML private Menu linesMenu
    @FXML private Menu mongodbMenu
    @FXML private ToggleGroup rgDerivatives
    @FXML private CheckBox cxLoadOptionsHtml
    @FXML private CheckBox cxLoadStockHtml
    @FXML private TabPane myTabPane
    @FXML private CheckBox cxIsCloud
    @FXML private Label lblLocalMongodbUrl
    @FXML private Label lblSqlUrl
    //endregion

    final static int SAVE_TO_DATASTORE  = 1;
    final static int FETCH_FROM_DATASTORE  = 2;
    final static int COMMENTS  = 3;


    //region FXML Methods
    public void initialize() {
        initContextInfo()
        initChoiceBoxTickers()
        initMenus()

        def initController = { controller, name, location, chart ->
            if (controller == null) return
            controller.setName(name)
            controller.setLocation(location)
            controller.setChart(chart)
            controller.setModel(facade)
            //controller.fibonacci1272extProperty().bind(fib1272extCheckMenu.selectedProperty())
            myListeners.add(controller);
        }

        initController candlesticksController, "Canclesticks", 1, candlesticksChart
        initController weeksController,"Weeks",2, weeklyChart
        initController obxCandlesticksController,"OBX Candlest.", 3, obxCandlesticksChart
        initController obxWeeksController, "OBX Weeks", 4, obxWeeklyChart

        initOptionsController()
    }

    void close(ActionEvent event)  {
        System.exit(0)
    }
    //endregion FXML Methods

    private void initContextInfo() {
        cxIsCloud.selected = windowDressingModel.cloud
        lblLocalMongodbUrl.text = 'MongoDB:' + windowDressingModel.mongodbHost
        lblSqlUrl.text = 'SQL: ' + sqldbUrl
        cxIsCloud.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                windowDressingModel.cloud = cxIsCloud.selected
            }
        });
    }
    private void initChoiceBoxTickers() {
        final ObservableList<Stock> cbitems = FXCollections.observableArrayList(facade.getTickers())
        cbTickers.setConverter(new StringConverter<Stock>() {
            @Override
            public String toString(Stock o) {
                return String.format("[%s] %s",o.getTicker(),o.getCompanyName())
            }

            @Override
            public Stock fromString(String s) {
                throw new NotImplementedException()
            }
        })
        cbTickers.getItems().addAll(cbitems)
        cbTickers.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue,
                                        Number value,
                                        Number newValue) {
                        setTicker(cbitems.get(newValue.intValue()))
                    }
                }
        )
    }

    private void initOptionsController() {
        def notifyOptionsController = {
            Object prop = cbTickers.valueProperty().get()
            if (prop == null) return
            Stock ticker = (Stock)prop
            optionsController.setTicker(ticker)
        }

        rgDerivatives.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue,
                                Toggle toggle,
                                Toggle toggle2) {
                notifyOptionsController()
            }
        });

        cxLoadOptionsHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadOptionsHtml.isSelected()) {
                    notifyOptionsController()
                }
            }
        });

        cxLoadStockHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadStockHtml.isSelected()) {
                    notifyOptionsController()
                }
            }
        });


        if (optionsController != null) {
            optionsController.selectedDerivativeProperty().bind(rgDerivatives.selectedToggleProperty())
            optionsController.selectedLoadStockProperty().bind(cxLoadStockHtml.selectedProperty())
            optionsController.selectedLoadDerivativesProperty().bind(cxLoadOptionsHtml.selectedProperty())
            optionsController.setModel(facade)
            optionsController.addDerivativesControllerListener(candlesticksController)
            optionsController.addDerivativesControllerListener(weeksController)
        }

    }

    private void initMenus() {

        def creteLinesMenuItem = {String title,  final int fibEvent ->
            MenuItem m = new MenuItem(title);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    int curloc = myTabPane.getSelectionModel().getSelectedIndex();
                    ChartCanvasLineEvent evt = new ChartCanvasLineEvent(curloc,fibEvent);
                    for (MainFrameControllerListener listener : myListeners) {
                        listener.onChartCanvasLineEvent(evt);
                    }
                }
            })
            return m
        }
        def createMongoDBMenuItem = {String title, final int mongoEventId ->
            MenuItem m = new MenuItem(title);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    int curloc = myTabPane.getSelectionModel().getSelectedIndex()
                    switch (mongoEventId) {
                        case SAVE_TO_DATASTORE:
                            SaveToMongoDBEvent event = new SaveToMongoDBEvent(curloc)
                            for (MainFrameControllerListener listener : myListeners) {
                                listener.onSaveToMongoDBEvent(event)
                            }
                            break
                        case FETCH_FROM_DATASTORE:
                            MainFrameControllerListener curListener = myListeners.find { ChartCanvasController x -> x.location == curloc }
                            if (curListener != null) {
                                List<DBObject> fibLines = facade.getWindowDressingModel().fetch(
                                        new ChartWindowsDressingContext(
                                                collection: ChartWindowDressingModel.MONGO_ALL,
                                                ticker: currentTicker.getTicker(),
                                                loc: curloc))
                                log.info(String.format("Fetching Fibonacci lines from mongodb for ticker: %s, location: %d, num lines: %d",
                                        currentTicker.getTicker(),
                                        curloc,
                                        fibLines == null ? 0 : fibLines.size()));
                                curListener.onFetchFromMongoDBEvent(new FetchFromMongoDBEvent(fibLines,null))
                            }
                            break
                        case COMMENTS:
                            ChartCanvasController ccc2 = myListeners.find { ChartCanvasController x -> x.location == curloc }

                            if (ccc2 != null) {
                                MaunaloaContext ctx2 = new MaunaloaContext()
                                ctx2.setWindowDressingModel(windowDressingModel)
                                ctx2.setLines(ccc2.getLines())
                                FxUtils.loadApp(ctx2,"/ChartCommentsDialog.fxml","MongoDB Comments")
                            }

                            break
                    }
                }
            })
            return m
        }
        def createLevelsMenuItem = {final String title ->
            MenuItem m = new MenuItem(title)

            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int curloc = myTabPane.getSelectionModel().getSelectedIndex()

                    MaunaloaContext ctx = new MaunaloaContext()

                    ctx.setLocation(curloc)

                    ctx.setListeners(myListeners)

                    FxUtils.loadApp(ctx,"/NewLevelDialog.fxml", title)
                }
            })
            return m
        }

        linesMenu.getItems().addAll(
                createLevelsMenuItem("New Level"),
                new SeparatorMenuItem(),
                creteLinesMenuItem("New Fibonacci Line", ChartCanvasLineEvent.NEW_LINE),
                new SeparatorMenuItem(),
                creteLinesMenuItem("Hide selected Lines", ChartCanvasLineEvent.HODE_SEL_LINES),
                creteLinesMenuItem("Hide all Lines", ChartCanvasLineEvent.HODE_ALL_LINES),
                new SeparatorMenuItem(),
                creteLinesMenuItem("Delete selected Lines", ChartCanvasLineEvent.DELETE_SEL_LINES),
                creteLinesMenuItem("Delete all Lines", ChartCanvasLineEvent.DELETE_ALL_LINES))

        mongodbMenu.getItems().addAll(
                createMongoDBMenuItem("Fetch from datastore", FETCH_FROM_DATASTORE),
                new SeparatorMenuItem(),
                createMongoDBMenuItem("Comments", COMMENTS),
                new SeparatorMenuItem(),
                createMongoDBMenuItem("Save to datastore", SAVE_TO_DATASTORE))

    }


    void setTicker(Stock stock) {
        println stock.companyName
        currentTicker = stock
        switch (stock.tickerCategory) {
            case 1:
                candlesticksController.ticker = stock
                weeksController.ticker = stock
                optionsController.ticker = stock
                break;
            case 2:
                obxCandlesticksController.ticker = stock
                obxWeeksController.ticker = stock
                break;
        }
    }

    Stock currentTicker
    MaunaloaChart candlesticksChart
    MaunaloaChart weeklyChart
    MaunaloaChart obxCandlesticksChart
    MaunaloaChart obxWeeklyChart
    MaunaloaFacade facade
    String sqldbUrl
    ChartWindowDressingModel windowDressingModel

    private Logger log = Logger.getLogger(getClass().getPackage().getName())
    //private CheckMenuItem fib1272extCheckMenu
    private List<MainFrameControllerListener> myListeners = new ArrayList<>()

}
