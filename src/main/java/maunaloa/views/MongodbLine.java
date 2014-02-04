package maunaloa.views;

import com.mongodb.BasicDBObject;
import maunaloa.controllers.ChartCanvasController;
import maunaloa.models.MaunaloaFacade;
import maunaloax.domain.MongoDBResult;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 9/30/13
 * Time: 11:19 AM
 */
public interface MongodbLine {
    static int P1 = 1;
    static int P2 = 2;
    static int LOC_CNDL = 1;
    static int LOC_WEEKS = 2;
    ObjectId getMongodbId();
    void setMongodbId(ObjectId id);
    boolean getActive();
    void setActive(boolean value);
    BasicDBObject coord(int pt);
    void addComment(String comment);
    List<String> getComments();
    MongoDBResult save(ChartCanvasController controller);
    String whoAmI();
}
