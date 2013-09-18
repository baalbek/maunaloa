package maunaloa.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Toggle;
import maunaloa.events.DerivativesControllerListener;
import oahu.financial.Stock;
import oahux.models.MaunaloaFacade;

import java.util.Map;

public interface DerivativesController {
    ObjectProperty<Toggle> selectedDerivativeProperty();
    BooleanProperty selectedLoadStockProperty();
    BooleanProperty selectedLoadDerivativesProperty();
    void setTicker(Stock ticker);
    void setModel(MaunaloaFacade model);
    void setMenus(Map<String,Menu> menus);
    void addDerivativesCalculatedListener(DerivativesControllerListener listener);
}
