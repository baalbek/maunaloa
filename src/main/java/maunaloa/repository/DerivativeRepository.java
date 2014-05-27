package maunaloa.repository;

import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;

import java.util.Collection;

/**
 * Created by rcs on 4/15/14.
 */
public interface DerivativeRepository {
    Collection<DerivativeFx> calls(String ticker);
    Collection<DerivativeFx> puts(String ticker);
    StockPrice spot(String ticker);
}
