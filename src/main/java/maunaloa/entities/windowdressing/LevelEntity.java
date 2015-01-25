package maunaloa.entities.windowdressing;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.controllers.CommentsController;
import maunaloa.repository.WindowDressingRepository;
import maunaloa.service.FxUtils;
import maunaloa.service.Logx;
import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.DraggableTextArea;
import maunaloa.views.charts.LevelLine;
import oahu.functional.Procedure3;
import oahux.chart.IRuler;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.04.14
 * Time: 13:35
 */
public class LevelEntity extends AbstractWindowDressingItem implements ChartItem {

    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private double levelValue;
    private final IRuler vruler;
    private LevelLine levelLine;

    public LevelEntity(ObjectId oid,
                       String ticker,
                       int location,
                       double levelValue,
                       IRuler vruler) {
        this(ticker,location,levelValue,vruler);
        this.oid = oid;
        entityStatusProperty().set(StatusCodes.ENTITY_CLEAN);
    }

    public LevelEntity(String ticker,
                       int location,
                       double levelValue,
                       IRuler vruler) {
        super(ticker,location);
        this.levelValue = levelValue;
        this.vruler = vruler;
        entityStatusProperty().set(StatusCodes.ENTITY_NEW);
    }
    //endregion Init

    //region Events
    private Procedure3<LevelEntity,CommentEntity,Boolean> onAddedNewComment;
    public void setOnAddedNewComment(Procedure3<LevelEntity,CommentEntity,Boolean> onAddedNewComment) {
        this.onAddedNewComment = onAddedNewComment;
    }
    //endregion Events

    //region Interface ChartItem
    private Node _view;
    @Override
    public Node view() {
        if (_view == null) {
            levelLine = new LevelLine(levelValue, vruler);
            levelLine.setOnMouseReleased((evt,anchor) -> {
                //int curStatus = isClean();
                //entityStatusProperty().set(curStatus);
                //levelLine.updateColorFor(curStatus);
                cleanStatusProperty().set(isClean());
            });
            levelLine.setOnMouseReleasedShift(evt -> {
                loadCommentsDialog();
            });
            _view = levelLine.view();
        }
        return _view;
    }

    private DraggableTextArea _commentsView;
    @Override
    public Optional<Node> commentsView() {
        if (_commentsView == null) {
            if (getComments().isPresent() == false) {
                return Optional.empty();
            }

            StringBuilder buf = new StringBuilder();

            getComments().get().stream().forEach(c -> {
                buf.append(c);
            });

            _commentsView = new DraggableTextArea(buf.toString(),
                                                    levelLine.getLine().getStartX()+15,
                                                    levelLine.getLine().getStartY()+5);
            _commentsView.setOnMouseDoubleClick(e -> {
                loadCommentsDialog();
            });
        }
        return Optional.of(_commentsView.view());
    }

    private MaunaloaStatus maunaloaStatus;
    @Override
    public MaunaloaStatus getStatus() {
        if (maunaloaStatus == null) {
            return new MaunaloaStatus(
                    entityStatusProperty(),
                    levelLine.statusProperty(),
                    cleanStatusProperty());
        }
        return maunaloaStatus;
    }

    @Override
    public void saveToRepos(WindowDressingRepository repos) {
        repos.saveLevel(this);
    }

    @Override
    public void removeFrom(ObservableList<Node> container) {
        if (_view != null) {
            container.remove(_view);
        }
        if (_commentsView != null) {
            container.remove(_commentsView.view());
        }
    }
    @Override
    public void setEntityStatus(int value) {
        if ((value == StatusCodes.ENTITY_TO_BE_INACTIVE) &&
                (oid == null)) {
            return;
        }
        entityStatusProperty().set(value);
        levelLine.updateColorFor(value);
        if (value == StatusCodes.ENTITY_CLEAN) {
            levelLine.setStatus(StatusCodes.UNSELECTED);
        }
    }

    //endregion Interface ChartItem

    //region Public Methods
    public double getLevelValue() {
        return levelLine.getLevelValue();
    }
    @Override
    public String toString() {
        int numComments = comments == null ? 0 : comments.size();
        return oid == null ?
                String.format("New level, comments: %d", numComments) :
                String.format("(%s) Level with status %d, comments: %d", oid, entityStatusProperty().get(),numComments);
    }
    //endregion Public Methods

    //region Private/Protected
    private void loadCommentsDialog() {
        FxUtils.loadApp("/ChartCommentsDialog.fxml", "New Comment",
                new CommentsController(this, comment -> {
                    boolean wasFirstComment = addComment(comment);
                    Logx.debug(log, () -> {
                        return String.format("wasFirstComment %s", wasFirstComment);
                    });

                    if (wasFirstComment == true) {
                        if (onAddedNewComment != null) {
                            onAddedNewComment.apply(this,comment,wasFirstComment);
                        }
                    }
                    else {
                        _commentsView.getTextArea().appendText(comment.toString());
                    }
                }));
    }

    private boolean isClean() {
        /*if (oid == null) {
            return StatusCodes.ENTITY_NEW;
        }
        else {
            if (Math.abs(levelLine.getLevelValue() - levelValue) >= 0.05) {
                return StatusCodes.ENTITY_DIRTY;
            }
            else {
                return StatusCodes.ENTITY_CLEAN;
            }
        }*/
        return true;
    }


    //endregion
}
