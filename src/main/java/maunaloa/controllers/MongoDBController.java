package maunaloa.controllers;

import maunaloa.events.MainFrameControllerListener;
import maunaloa.models.MaunaloaFacade;
import oahu.financial.Stock;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/5/13
 * Time: 7:10 PM
 */
public interface MongoDBController {
    void addListener(MainFrameControllerListener listener);
    void setListeners(List<MainFrameControllerListener> listeners);
    void setFacade(MaunaloaFacade facade);
    void setTicker(Stock stock);
    void setLocation(int location);

}
