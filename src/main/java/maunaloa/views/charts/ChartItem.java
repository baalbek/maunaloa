package maunaloa.views.charts;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.repository.WindowDressingRepository;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 24.04.14
 * Time: 15:14
 */
public interface ChartItem {
    //public enum ChartItemStatus { NA, SELECTED, DIRTY };
    Node view();
    Optional<Node> commentsView();
    void setEntityStatus(int value);
    MaunaloaStatus getStatus();
    void saveToRepos(WindowDressingRepository repos);
    Optional<List<CommentEntity>> getComments();
    void removeFrom(ObservableList<Node> container);
}
