package maunaloa.repository.impl;

import maunaloa.derivatives.DerivativeFxImpl;
import maunaloa.repository.DerivativeRepository;
import oahu.financial.DerivativePrice;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;
import ranoraraku.beans.StockBean;
import ranoraraku.beans.StockPriceBean;
import ranoraraku.beans.options.DerivativeBean;
import ranoraraku.beans.options.DerivativePriceBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by rcs on 18.01.16.
 *
 */

public class SimpleDerivativeRepository implements DerivativeRepository {
    @Override
    public Collection<DerivativeFx> getCalls(String ticker) {
        Collection<DerivativeFx> result = new ArrayList<>();

        StockBean stock = new StockBean();
        stock.setTicker(ticker);
        StockPriceBean stockPrice = new StockPriceBean(LocalDate.now(), 100,120,90,105, 1000000);
        stockPrice.setStock(stock);

        java.util.Date exp = new java.util.Date();
        exp.setYear(2016 - 1900);
        exp.setMonth(6);
        exp.setDate(1);

        OptionCalculator calc = new DummyOptionCalculator(); //new vega.financial.calculator.BlackScholes();

        DerivativeBean derivative = new DerivativeBean("NHY6F30", 1, 35, exp, stock);

        DerivativePriceBean dPrice = new DerivativePriceBean(stockPrice,derivative,10,12,calc);

        DerivativeFx fx = new DerivativeFxImpl(dPrice,calc);
        result.add(fx);
        return result;
    }

    @Override
    public Collection<DerivativeFx> getPuts(String ticker) {
        return null;
    }

    @Override
    public Optional<StockPrice> getSpot(String ticker) {
        return null;
    }
}

class DummyOptionCalculator implements OptionCalculator {

    @Override
    public double delta(DerivativePrice d) {
        return 0;
    }

    @Override
    public double spread(DerivativePrice d) {
        return 0;
    }

    @Override
    public double breakEven(DerivativePrice d) {
        return 0;
    }

    @Override
    public double stockPriceFor(double optionPrice, DerivativePrice o) {
        return 0;
    }

    @Override
    public double iv(DerivativePrice d, int priceType) {
        return 0;
    }

    @Override
    public double ivCall(double spot, double strike, double yearsExpiry, double optionPrice) {
        return 0;
    }

    @Override
    public double ivPut(double spot, double strike, double yearsExpiry, double optionPrice) {
        return 0;
    }

    @Override
    public double callPrice(double spot, double strike, double yearsExpiry, double sigma) {
        return 0;
    }

    @Override
    public double putPrice(double spot, double strike, double yearsExpiry, double sigma) {
        return 0;
    }
}

