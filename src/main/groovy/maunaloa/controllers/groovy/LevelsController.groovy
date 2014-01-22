package maunaloa.controllers.groovy

import com.mongodb.DBObject
import javafx.scene.shape.Line
import maunaloa.events.NewLevelEvent
import maunaloa.events.mongodb.FetchFromMongoDBEvent
import maunaloa.events.mongodb.SaveToMongoDBEvent
import maunaloa.views.CanvasGroup
import maunaloa.views.FibonacciDraggableLine
import maunaloa.views.Level
import maunaloa.views.MongodbLine
import oahu.financial.Stock
import oahux.chart.IRuler
import org.bson.types.ObjectId

/**
 * Created by rcs on 1/15/14.
 */
class LevelsController extends ChartCanvasControllerHelper {
    private Map<Stock,List<CanvasGroup>> levels = new HashMap<>()

    public void onNewLevelEvent(NewLevelEvent evt) {

    }

    void onSaveToMongoDBEvent(SaveToMongoDBEvent event) {

    }

    void onFetchFromMongoDBEvent(FetchFromMongoDBEvent event) {
        IRuler vruler = parent.getVruler()
        IRuler hruler = parent.getHruler()
        def createLineFromDBObject = { DBObject obj ->
            return null
        }
        for (DBObject o : event.getLevels()) {
            Line line = createLineFromDBObject(o)
            MongodbLine level = null

            level.setMongodbId((ObjectId)o.get("_id"))

            updateMyPaneLines((CanvasGroup) level, levels)
        }
    }
}
