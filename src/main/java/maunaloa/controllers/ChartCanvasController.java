package maunaloa.controllers;

import maunaloa.repository.StockRepository;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import oahux.chart.MaunaloaChart;
import oahux.controllers.MaunaloaChartViewModel;

import java.util.Collection;

/**
 * Created by rcs on 4/13/14.
 */
public class ChartCanvasController implements MaunaloaChartViewModel {
    private MaunaloaChart chart;
    private String name;
    private int location;
    private Stock stock;
    private IRuler vruler;
    private IRuler hruler;

    private StockRepository stockRepository;

    //region Properties
    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(int location) {
        this.location = location;

    }
    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
    }
    public void setStock(Stock stock) {
        this.stock = stock;
    }
    public StockRepository getStockRepository() {
        return stockRepository;
    }

    public void setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    //endregion Properties


    //region MaunaloaChartViewModel
    @Override
    public Collection<StockPrice> stockPrices(int period) {
        return getStockRepository().stockPrices(getStock().getTicker(),-1);
    }

    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public IRuler getVruler() {
        return vruler;
    }

    @Override
    public void setVruler(IRuler ruler) {
        this.vruler = ruler;
    }

    @Override
    public IRuler getHruler() {
        return hruler;
    }

    @Override
    public void setHruler(IRuler ruler) {
        this.hruler = ruler;
    }

    //endregion MaunaloaChartViewModel
}
