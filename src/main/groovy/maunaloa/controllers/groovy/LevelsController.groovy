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

    void onSaveToMongoDBEvent(SaveToMongoDBEvent event) {
        Stock stock = parent.getTicker()
        List<CanvasGroup> lines = levels.get(stock)
        lines.each  { CanvasGroup line ->
            MongodbLine mongoLine = (MongodbLine)line
            MongoDBResult result = mongoLine.save(parent)
            if (result.isOk()) {
                log.info(String.format("(%s) Successfully saved level with _id: %s to location: %d",
                        stock.getTicker(),
                        result.getObjectId(),
                        event.getLocation()))
            }
            else {
                log.error(String.format("(Saving level %s, %d) %s",
                        stock.getTicker(),
                        event.getLocation(),
                        result.getWriteResult().getError()))
            }
        }
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
