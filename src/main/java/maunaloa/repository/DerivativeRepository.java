package maunaloa.repository;

import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by rcs on 09.01.16.
 *
 */
public class DerivativeRepository {
    public Collection<DerivativeFx> getCalls(String ticker) {
        return new ArrayList<>();
    }

    public Collection<DerivativeFx> getPuts(String ticker) {
        return new ArrayList<>();
    }

    public Optional<StockPrice> getSpot(String ticker) {
        return Optional.empty();
    }
}
