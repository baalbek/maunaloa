package maunaloa.controllers.groovy

import com.mongodb.DBObject
import javafx.scene.shape.Line
import maunaloa.events.NewLevelEvent
import maunaloa.events.mongodb.FetchFromMongoDBEvent
import maunaloa.events.mongodb.SaveToMongoDBEvent
import maunaloa.views.CanvasGroup
import maunaloa.views.Level
import maunaloa.views.MongodbLine
import maunaloax.domain.MongoDBResult
import oahu.financial.Stock
import oahux.chart.IRuler
import org.bson.types.ObjectId

/**
 * Created by rcs on 1/15/14.
 */
class LevelsController extends ChartCanvasControllerHelper {
    private Map<Stock,List<CanvasGroup>> levels = new HashMap<>()

    void deleteLines(boolean deleteAll) {
        deleteLines(deleteAll, levels)
    }

    void clearLines() {
        clearLines(levels)
    }

    void refreshLines() {
        refreshLines(levels)
    }

    public void onNewLevelEvent(NewLevelEvent evt) {
        Level level = new Level(evt.getValue(), parent.getVruler())
        updateMyPaneLines(level, levels)
    }

    void onFetchFromMongoDBEvent(FetchFromMongoDBEvent event) {
        IRuler vruler = parent.getVruler()
        def createLevelFromDBObject = { DBObject obj ->
            //double y = vruler.calcPix(obj.get("value"))
            double y = obj.get("value")
            log.info(String.format("Fetched Level from MongoDb with value: %.2f", y))
            new Level(y, vruler)
        }
        for (DBObject o : event.getLevels()) {

            Level level = createLevelFromDBObject(o)

            level.setMongodbId((ObjectId)o.get("_id"))

            updateMyPaneLines(level, levels)
        }
    }

    void onSaveToMongoDBEvent(SaveToMongoDBEvent event) {
        onSaveToMongoDBEvent(event, levels)
    }
}
