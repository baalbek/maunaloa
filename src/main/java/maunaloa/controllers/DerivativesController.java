package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Toggle;
import maunaloa.events.DerivativesControllerListener;
import oahu.financial.Stock;
import oahux.models.MaunaloaFacade;

public interface DerivativesController {
    ObjectProperty<Toggle> selectedDerivativeProperty();
    BooleanProperty selectedLoadStockProperty();
    BooleanProperty selectedLoadDerivativesProperty();
    void setTicker(Stock ticker);
    void setModel(MaunaloaFacade model);
    void setMenuBar(MenuBar menuBar);
    void addDerivativesCalculatedListener(DerivativesControllerListener listener);
}
