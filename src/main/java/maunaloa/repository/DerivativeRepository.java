package maunaloa.repository;

import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;

import java.util.Collection;

/**
 * Created by rcs on 4/15/14.
 *
 */
public interface DerivativeRepository {
    StockPrice getSpot(String ticker);
    Collection<DerivativeFx> getCalls(String ticker);
    Collection<DerivativeFx> getPuts(String ticker);
    //StockPrice spot(String ticker);
    void invalidate();
    //void registerOptionPurchase(OptionPurchaseBean purchase);
    //void registerOptionPurchase(DerivativePrice purchase, int purchaseType, int volume);
}
