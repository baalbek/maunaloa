package maunaloa.views;

import javafx.scene.Node;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 8/8/13
 * Time: 11:53 PM
 */
public interface CanvasGroup {
    int NORMAL = 0;
    int SELECTED = 1;
    int SAVED_TO_DB = 2;
    Node view();
    void setStatus(int status);
    int getStatus();
}
