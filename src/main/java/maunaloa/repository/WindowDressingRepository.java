package maunaloa.repository;

import maunaloa.entities.windowdressing.FibLineEntity;
import maunaloa.entities.windowdressing.LevelEntity;
import maunaloa.views.charts.ChartItem;
import oahu.domain.Tuple;
import oahux.chart.IRuler;

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
    List<ChartItem> fetchFibLines(String ticker, int location, int status, Tuple<IRuler> rulers);
    List<ChartItem> fetchLevels(String ticker, int location, int status, IRuler vruler);
    void saveOrUpdate(ChartItem item);
    void saveFibonacci(FibLineEntity entity);
    void saveLevel(LevelEntity entity);
    boolean isCloud();
    void setCloud(boolean isCloud);
    // void invalidate(int whichOnes);
}
