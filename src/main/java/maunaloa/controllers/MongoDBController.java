package maunaloa.controllers;

import maunaloa.events.MongoDBControllerListener;
import maunaloa.models.MaunaloaFacade;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/5/13
 * Time: 7:10 PM
 */
public interface MongoDBController {
    void addListener(MongoDBControllerListener listener);
    void setListeners(List<MongoDBControllerListener> listeners);
    void setFacade(MaunaloaFacade facade);

}
