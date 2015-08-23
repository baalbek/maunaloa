package maunaloa.entities.windowdressing;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import maunaloa.StatusCodes;
import maunaloa.entities.MaunaloaEntity;
import maunaloa.service.Logx;
import oahux.controllers.ControllerEnum;
import org.apache.log4j.Logger;
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
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    protected ObjectId oid;
    protected final String ticker;
    protected final ControllerEnum location;

    protected List<CommentEntity> comments;

    public AbstractWindowDressingItem(String ticker,
                                      ControllerEnum location) {
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

    public ControllerEnum getLocation() {
        return location;
    }

    protected IntegerProperty _entityStatusProperty;
    public IntegerProperty entityStatusProperty() {
        if (_entityStatusProperty == null) {
            _entityStatusProperty = new SimpleIntegerProperty(StatusCodes.NA);
        }
        return _entityStatusProperty;
    }
    protected BooleanProperty _cleanStatusProperty;
    public BooleanProperty cleanStatusProperty() {
        if (_cleanStatusProperty == null) {
            _cleanStatusProperty = new SimpleBooleanProperty(true);
        }
        return _cleanStatusProperty;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }
    public Optional<List<CommentEntity>> getComments() {
        if ((comments == null) || (comments.size() == 0)) {
            return Optional.empty();
        }
        else {
            return Optional.of(comments);
        }
    }
    protected boolean addComment(CommentEntity comment) {
        boolean result = false;
        if (comments == null) {
            comments = new ArrayList<>();
            result = true;
            Logx.debug(log, () -> {
                return oid == null ? "(New Level) Comments == null" :
                        String.format("(%s) Comments == null", oid);
            });
        }
        else if (comments.size() == 0) {
            result = true;
            Logx.debug(log, () -> {
                return oid == null ? "(New Level) Comments size == 0" :
                        String.format("(%s) Comments size == 0", oid);
            });
        }
        comments.add(comment);
        return result;
    }
}
