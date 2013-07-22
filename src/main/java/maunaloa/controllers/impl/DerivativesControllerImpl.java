package maunaloa.controllers.impl;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import maunaloa.controllers.DerivativesController;
import maunaloa.views.DraggableLine;
import maunaloa.views.FibonacciDraggableLine;
import oahux.chart.MaunaloaChart;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 */
public class DerivativesControllerImpl implements DerivativesController, ChartViewModel {
    //region FXML
    @FXML private TableView<DerivativeBean> derivativesTableView;

    @FXML private TableColumn<DerivativeBean, String> colOpName;
    @FXML private TableColumn<DerivativeBean, Boolean> colSelected;
    @FXML private TableColumn<DerivativeBean, Date> colExpiry;

    @FXML private TableColumn<CalculatedDerivativeBean, Double> colBuy;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colSell;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colIvBuy;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colIvSell;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colSpread;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colDelta;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colBreakEven;

    @FXML private TableColumn<CalculatedDerivativeBean, Double> colDays;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colRisc;
    @FXML private TableColumn<CalculatedDerivativeBean, Double> colSpRisc;

    @FXML private ToggleGroup rgDerivatives;

    @FXML private ChoiceBox cbTickers;
    @FXML private Pane paneCandlesticks;
    @FXML private VBox containerCandlesticks;
    @FXML private Pane paneWeeks;
    @FXML private VBox containerWeeks;
    @FXML private Canvas myCanvas;
    @FXML private Canvas myCanvas2;
    @FXML private CheckBox cxLoadOptionsHtml;
    @FXML private CheckBox cxLoadStockHtml;


    @FXML private TextField txSpot;
    @FXML private TextField txOpen;
    @FXML private TextField txHi;
    @FXML private TextField txLo;
    //endregion FXML


    //region Init

    List<DraggableLine> paneCandlesticksLines = new ArrayList<>();
    List<DraggableLine> paneWeeksLines = new ArrayList<>();

    private MaunaloaChart chart;
    private MaunaloaChart chart2;
    private StockTicker stockTicker;

    private ObservableList<DerivativeBean> beans;
    private String ticker = null;
    private List<String> tickers;

    private StockBean stock;

    private MaunaloaFacade facade;

    final ObjectProperty<Line> lineA = new SimpleObjectProperty<>();

    public DerivativesControllerImpl() {
    }

    public DerivativesControllerImpl(MaunaloaFacade facade) {
        this.facade = facade;
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
        if (cxLoadStockHtml.isSelected()) {
            stock.assign(facade.spot(ticker));
        }
        draw();
    }

    public void close(ActionEvent event)  {
        System.exit(0);
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

    //region Fibonacci
    public void activateFibA() {
        paneCandlesticks.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                Line line = new Line(x, y, x, y);
                paneCandlesticks.getChildren().add(line);
                lineA.set(line);
            }
        });
        paneCandlesticks.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Line line = lineA.get();
                if (line != null) {
                    line.setEndX(event.getX());
                    line.setEndY(event.getY());
                }
            }
        });
        paneCandlesticks.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Line line = lineA.get();
                if (line != null) {
                    paneCandlesticks.getChildren().remove(line);
                    DraggableLine fibLine = new FibonacciDraggableLine(line,getRuler(ChartViewModel.CHART_A1_VRULER));
                    paneCandlesticksLines.add(fibLine);
                    paneCandlesticks.getChildren().add(fibLine.view());
                }
                lineA.set(null);
            }
        });
    }
    public void deactivateFibA() {
        paneCandlesticks.setOnMousePressed(null);
        paneCandlesticks.setOnMouseDragged(null);
        paneCandlesticks.setOnMouseReleased(null);
    }
    public void clearFibA() {
        for (DraggableLine l : paneCandlesticksLines) {
            paneCandlesticks.getChildren().remove(l.view());
        }
        paneCandlesticksLines.clear();
    }
    public void activateFibB() {
        paneWeeks.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                Line line = new Line(x, y, x, y);
                paneWeeks.getChildren().add(line);
                lineA.set(line);
            }
        });
        paneWeeks.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Line line = lineA.get();
                if (line != null) {
                    line.setEndX(event.getX());
                    line.setEndY(event.getY());
                }
            }
        });
        paneWeeks.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Line line = lineA.get();
                if (line != null) {
                    paneWeeks.getChildren().remove(line);
                    DraggableLine fibLine = new FibonacciDraggableLine(line,getRuler(ChartViewModel.CHART_A1_VRULER));
                    paneWeeksLines.add(fibLine);
                    paneWeeks.getChildren().add(fibLine.view());
                }
                lineA.set(null);
            }
        });
    }
    public void deactivateFibB() {
        paneWeeks.setOnMousePressed(null);
        paneWeeks.setOnMouseDragged(null);
        paneWeeks.setOnMouseReleased(null);
    }
    public void clearFibB() {
        for (DraggableLine l : paneWeeksLines) {
            paneWeeks.getChildren().remove(l.view());
        }
        paneWeeksLines.clear();
    }
    //endregion Fibonacci

    //region Interface methods


    @Override
    public  void draw() {
        if (ticker == null) return;
        chart.draw(myCanvas);
        chart2.draw(myCanvas2);
    }


    @Override
    public Collection<StockBean> stockPrices(int period) {
        return facade.stockPrices(ticker, period);
    }

    @Override
    public String getTicker() {
        return ticker;
    }

    private Map<Integer,IRuler> _rulerMap = new HashMap<>();
    @Override
    public IRuler getRuler(int id) {
        return _rulerMap.get(id);
    }

    @Override
    public void setRuler(int id, IRuler ruler) {
        _rulerMap.put(id, ruler);
    }

    //endregion  Interface methods



    //region Initialization methods
    public void initialize() {
        initChoiceBoxTickers();
        initGrid();
        initMyCanvas();

        cxLoadOptionsHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadOptionsHtml.isSelected()) {
                    ObservableList<DerivativeBean> items = derivatives();
                    if (items != null) {
                        derivativesTableView.getItems().setAll(items);
                    }
                }
            }
        });
        cxLoadStockHtml.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (cxLoadStockHtml.isSelected()) {
                    stock.assign(facade.spot(ticker));
                }
            }
        });

        rgDerivatives.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue,
                                Toggle toggle,
                                Toggle toggle2) {
                String ticker = cbTickers.valueProperty().get().toString();
                String opType = observableValue.getValue().getUserData().toString();
                ObservableList<DerivativeBean> items = fetchDerivativesForTicker(ticker, opType);
                derivativesTableView.getItems().setAll(items);
            }
        });


        stock = new StockBean();
        StringConverter<? extends Number> converter =  new DoubleStringConverter();
        Bindings.bindBidirectional(txSpot.textProperty(), stock.clsProperty(), (StringConverter<Number>) converter);
        Bindings.bindBidirectional(txOpen.textProperty(), stock.opnProperty(), (StringConverter<Number>) converter);
        Bindings.bindBidirectional(txHi.textProperty(), stock.hiProperty(), (StringConverter<Number>) converter);
        Bindings.bindBidirectional(txLo.textProperty(), stock.loProperty(), (StringConverter<Number>) converter);

    }

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
        colSell.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("sell"));

        colIvBuy.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("ivBuy"));
        colIvSell.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("ivSell"));
        colDelta.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("delta"));
        colBreakEven.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("breakeven"));
        colSpread.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("spread"));
        colDays.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("days"));
        colRisc.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("risk"));
        colSpRisc.setCellValueFactory(new PropertyValueFactory<CalculatedDerivativeBean, Double>("stockPriceRisk"));
    }

    private void initMyCanvas() {
        InvalidationListener listener =     new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                if (ticker == null) return;
                chart.draw(myCanvas);
            }
        };

        InvalidationListener listener2 =     new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                if (ticker == null) return;
                chart2.draw(myCanvas2);
            }
        };

        myCanvas.widthProperty().bind(containerCandlesticks.widthProperty());
        myCanvas.heightProperty().bind(containerCandlesticks.heightProperty());

        myCanvas2.widthProperty().bind(containerWeeks.widthProperty());
        myCanvas2.heightProperty().bind(containerWeeks.heightProperty());

        myCanvas.widthProperty().addListener(listener);
        myCanvas.heightProperty().addListener(listener);
        myCanvas2.widthProperty().addListener(listener2);
        myCanvas2.heightProperty().addListener(listener2);

    }




    //endregion  Initialization methods

    //region Properties

    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
        this.chart.setViewModel(this);
    }


    public void setChart2(MaunaloaChart chart2) {
        this.chart2 = chart2;
        this.chart2.setViewModel(this);
    }

    public List<String> getTickers() {

        return stockTicker != null ?
                stockTicker.getTickers() :
                tickers;
    }

    public void setTickers(List<String> tickers) {
        this.tickers = tickers;
    }

    public StockTicker getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(StockTicker stockTicker) {
        this.stockTicker = stockTicker;
    }
    //endregion
}
