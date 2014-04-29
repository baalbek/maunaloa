package maunaloa.views.charts;

import javafx.scene.Node;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 24.04.14
 * Time: 15:14
 */
public interface ChartItem {
    public static int NA = 0;
    Node view();
    int getStatus();
    void setStatus(int value);
}
