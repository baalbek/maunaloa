package maunaloa.repository;

import maunaloa.entities.MaunaloaEntity;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.entities.windowdressing.FibLineEntity;
import maunaloa.entities.windowdressing.LevelEntity;
import maunaloa.views.charts.ChartItem;
import oahu.dto.Tuple;
import oahu.dto.Tuple2;
import oahu.functional.Procedure3;
import oahux.chart.IRuler;
import oahux.controllers.ControllerEnum;

import java.time.LocalDate;
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
    List<ChartItem> fetchFibLines(String ticker,
                                  ControllerEnum location,
                                  int status,
                                  Tuple2<IRuler<LocalDate>,IRuler<Double>> rulers);
    List<ChartItem> fetchLevels(String ticker,
                                ControllerEnum location,
                                int status,
                                IRuler vruler,
                                Procedure3<LevelEntity,CommentEntity,Boolean> onAddedNewComment);
    List<CommentEntity> fetchComments(MaunaloaEntity parent);
    //List<CommentEntity> fetchComments(LocalDateTime fromDate, LocalDateTime toDate);
    void addComment(CommentEntity comment);
    void saveOrUpdate(ChartItem item);
    void saveFibonacci(FibLineEntity entity);
    void saveLevel(LevelEntity entity);
    void saveComments(List<CommentEntity> comments);
    boolean isCloud();
    void setCloud(boolean isCloud);

    // void invalidate(int whichOnes);
}
