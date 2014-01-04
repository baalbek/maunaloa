/**
 * Created by rcs on 1/4/14.
 */
package maunaloa.controllers.groovy

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.TabPane
import javafx.scene.control.ToggleGroup
import javafx.util.StringConverter
import maunaloa.controllers.ChartCanvasController
import maunaloa.controllers.DerivativesController
import maunaloa.controllers.MainFrameController
import maunaloa.events.MainFrameControllerListener
import maunaloa.models.MaunaloaFacade
import maunaloax.models.ChartWindowDressingModel
import oahu.exceptions.NotImplementedException
import oahu.financial.Stock
import oahux.chart.MaunaloaChart

class MainFrameController2  implements MainFrameController {
    //region FXML
    @FXML private ChartCanvasController obxCandlesticksController
    @FXML private ChartCanvasController obxWeeksController
    @FXML private ChartCanvasController candlesticksController
    @FXML private ChartCanvasController weeksController
    @FXML private DerivativesController optionsController
    @FXML private ChoiceBox cbTickers
    @FXML private MenuBar myMenuBar
    @FXML private Menu fibonacciMenu
    @FXML private Menu levelsMenu
    @FXML private Menu mongodbMenu
    @FXML private ToggleGroup rgDerivatives
    @FXML private CheckBox cxLoadOptionsHtml
    @FXML private CheckBox cxLoadStockHtml
    @FXML private TabPane myTabPane
    //endregion


    //region FXML Methods
    public void initialize() {
        initChoiceBoxTickers()

        def initController = { controller, name, location, chart ->
            controller.setName(name)
            controller.setLocation(location)
            controller.setChart(chart)
            controller.setModel(facade)
            //controller.fibonacci1272extProperty().bind(fib1272extCheckMenu.selectedProperty())
            myListeners.add(controller);
        }

        initController candlesticksController, "Canclesticks", 2, candlesticksChart
        initController weeksController,"Weeks",2, weeklyChart
        initController obxCandlesticksController,"OBX Candlest.", 3, obxCandlesticksChart
        initController obxWeeksController, "OBX Weeks", 4, obxWeeklyChart
    }

    void close(ActionEvent event)  {
        System.exit(0)
    }
    //endregion FXML Methods


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

    void setTicker(Stock stock) {
        println stock.companyName
        currentTicker = stock.ticker
        switch (stock.tickerCategory) {
            case 1:
                candlesticksController.ticker = stock
                weeksController.ticker = stock
                //optionsController.ticker = stock
                break;
            case 2:
                obxCandlesticksController.ticker = stock
                obxWeeksController.ticker = stock
                break;
        }
    }

    String currentTicker
    MaunaloaChart candlesticksChart
    MaunaloaChart weeklyChart
    MaunaloaChart obxCandlesticksChart
    MaunaloaChart obxWeeklyChart
    MaunaloaFacade facade
    ChartWindowDressingModel windowDressingModel


    private List<MainFrameControllerListener> myListeners = new ArrayList<>()
}
