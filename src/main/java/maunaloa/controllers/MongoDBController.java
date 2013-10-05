package maunaloa.controllers;

import maunaloa.events.MongoDBControllerListener;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/5/13
 * Time: 7:10 PM
 */
public interface MongoDBController {
    void setListener(MongoDBControllerListener listener);

}
