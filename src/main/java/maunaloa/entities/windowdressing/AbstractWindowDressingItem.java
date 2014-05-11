package maunaloa.entities.windowdressing;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import maunaloa.entities.MaunaloaEntity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 03.05.14
 * Time: 12:48
 */
public abstract class AbstractWindowDressingItem implements MaunaloaEntity {
    protected ObjectId oid;
    protected final String ticker;
    protected final int location;
    protected List<CommentEntity> comments;


    public AbstractWindowDressingItem(String ticker,
                                      int location) {
        this.ticker = ticker;
        this.location = location;
    }
    @Override
    public ObjectId getOid() {
        return oid;
    }
    public void setOid(ObjectId oid) {
        this.oid = oid;
    }

    public String getTicker() {
        return ticker;
    }

    public int getLocation() {
        return location;
    }

    protected IntegerProperty _entityStatusProperty;
    public IntegerProperty entityStatusProperty() {
        if (_entityStatusProperty == null) {
            _entityStatusProperty = new SimpleIntegerProperty(recalcEntityStatus());
        }
        return _entityStatusProperty;
    }
    public Optional<List<CommentEntity>> getComments() {
        if ((comments == null) || (comments.size() == 0)) {
            return Optional.empty();
        }
        else {
            return Optional.of(comments);
        }
    }
    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }
    public void addComment(CommentEntity newComment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(newComment);
    }
    protected abstract int recalcEntityStatus();
}
