package maunaloa.entities.windowdressing;

import maunaloa.StatusCodes;
import maunaloa.entities.MaunaloaEntity;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

/**
 * Created by rcs on 5/10/14.
 */
public class CommentEntity implements MaunaloaEntity {
    private ObjectId oid;
    private String comment;
    private int entityStatus;

    private final MaunaloaEntity parent;
    private final LocalDateTime commentDate;

    public CommentEntity(MaunaloaEntity parent,
                         String comment,
                         LocalDateTime commentDate) {
        this.parent = parent;
        this.comment = comment;
        this.commentDate = commentDate;
        this.entityStatus = StatusCodes.ENTITY_NEW;
    }
    public CommentEntity(
                         MaunaloaEntity parent,
                         ObjectId oid,
                         String comment,
                         LocalDateTime commentDate) {
        this(parent,comment,commentDate);
        this.oid = oid;
        this.entityStatus = StatusCodes.ENTITY_CLEAN;
    }
    //region Properties

    @Override
    public ObjectId getOid() {
        return oid;
    }

    public void setOid(ObjectId oid) {
        this.oid = oid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        if (entityStatus == StatusCodes.ENTITY_CLEAN) {
            entityStatus = StatusCodes.ENTITY_DIRTY;
        }
    }

    public ObjectId getRefId() {
        return parent.getOid();
    }

    public LocalDateTime getCommentDate() {
        return commentDate;
    }

    public int getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(int entityStatus) {
        this.entityStatus = entityStatus;
    }
    //endregion Properties

    //region ChartItem
    /*@Override
    public Node view() {
        throw new NotImplementedException();
    }

    @Override
    public MaunaloaStatus getStatus() {
        throw new NotImplementedException();
    }

    @Override
    public void saveToRepos(WindowDressingRepository repos) {
        repos.saveComment(this);
    }*/
    //endregion ChartItem
}
