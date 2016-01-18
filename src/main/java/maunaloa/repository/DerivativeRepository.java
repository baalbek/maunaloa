package maunaloa.repository;

import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by rcs on 09.01.16.
 *
 */
public interface DerivativeRepository {
    Collection<DerivativeFx> getCalls(String ticker);
    Collection<DerivativeFx> getPuts(String ticker);
    Optional<StockPrice> getSpot(String ticker);
}
