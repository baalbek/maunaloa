package maunaloa.repository;

import oahu.financial.StockPrice;

public interface StockIndexRepository {
    StockPrice getSpot(String ticker);
    void invalidate();
}

