package maunaloa.repository;

import maunaloa.controllers.ControllerHub;
import maunaloa.entities.windowdressing.FibLine;
import maunaloa.views.charts.ChartItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 13:29
 */
public interface WindowDressingRepository {
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;
    public static final int BOTH = 3;
    public static final int FIBLINES = 1;
    public static final int LEVEL = 2;
    public static final int ALL_ITEMS = 3;
    List<ChartItem> fetchFibLines(String ticker, int location, int status);
    // void invalidate(int whichOnes);
    void setControllerHub(ControllerHub hub);
}
