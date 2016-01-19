package maunaloa.repository.impl;

import maunaloa.repository.DerivativeRepository;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;
import ranoraraku.beans.StockBean;
import ranoraraku.beans.StockPriceBean;
import ranoraraku.beans.options.DerivativeBean;

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
        DerivativeBean derivative = new DerivativeBean("NHY6F30", 1, 35, exp, stock);
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
