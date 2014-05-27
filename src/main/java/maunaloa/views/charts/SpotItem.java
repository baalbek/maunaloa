package maunaloa.views.charts;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.repository.WindowDressingRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by rcs on 5/27/14.
 */
public class SpotItem implements ChartItem{
    private Group group ;
    private MaunaloaStatus maunaloaStatus;

    @Override
    public Node view() {
        if (group == null) {
            group = new Group();
        }
        return group;
    }

    @Override
    public Optional<Node> commentsView() {
        return Optional.empty();
    }

    @Override
    public MaunaloaStatus getStatus() {
        if (maunaloaStatus == null) {
            IntegerProperty entStatus = new SimpleIntegerProperty(StatusCodes.NA);
            IntegerProperty lineStatus = new SimpleIntegerProperty(StatusCodes.NA);
            if (maunaloaStatus == null) {
                return new MaunaloaStatus(entStatus,lineStatus);
            }
        }
        return maunaloaStatus;
    }

    @Override
    public void saveToRepos(WindowDressingRepository repos) {
    }

    @Override
    public Optional<List<CommentEntity>> getComments() {
        return Optional.empty();
    }

    @Override
    public void removeFrom(ObservableList<Node> container) {
    }
}
