package maunaloa.controllers;

import oahu.financial.StockPrice;
import oahux.financial.DerivativeFx;

import java.util.List;

/**
 * Created by rcs on 5/19/14.
 */
public interface DerivativesControllerListener {
    void notifyDerivativesCalculated(List<DerivativeFx> calculated);
    void notifySpotUpdated(StockPrice spot);
    void notifySpotOptsEvent(List<DerivativeFx> selected);
}
