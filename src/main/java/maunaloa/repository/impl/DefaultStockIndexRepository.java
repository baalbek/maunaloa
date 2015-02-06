package maunaloa.repository.impl;

import oahu.financial.StockPrice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultStockIndexRepository implements StockIndexRepository {
    private Map<String,StockPrice> items;

    //region Interface StockIndexRepository 
    @Override
    public StockPrice getSpot(String ticker) {
        if (items == null) {
            items = new HashMap<>();
        }

        StockPrice result = items.get(ticker);
        if (result == null) {
            
        }
        return result;
    }
    @Override
    public void invalidate() {
    }
    //endregion Interface StockIndexRepository 

    //region Properties
    private EtradeDerivatives etrade;

    public EtradeDerivatives getEtrade() {
        return etrade;
    }

    public void setEtrade(EtradeDerivatives etrade) {
        this.etrade = etrade;
    }
    //endregion Properties
}
