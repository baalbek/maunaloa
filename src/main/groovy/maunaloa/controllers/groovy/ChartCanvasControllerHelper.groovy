package maunaloa.controllers.groovy

import maunaloa.controllers.ChartCanvasController
import maunaloa.events.mongodb.SaveToMongoDBEvent
import maunaloa.views.CanvasGroup
import maunaloa.views.MongodbLine
import maunaloax.domain.MongoDBResult
import oahu.financial.Stock
import org.apache.log4j.Logger

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.01.14
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
class ChartCanvasControllerHelper {
    ChartCanvasController parent

    protected Logger log = Logger.getLogger(getClass().getPackage().getName())

    protected void updateMyPaneLines(CanvasGroup line, Map<Stock,List<CanvasGroup>> myLines) {
        Stock curTicker = parent.getTicker()
        List<CanvasGroup> lines = myLines.get(curTicker)
        if (lines == null) {
            lines = new ArrayList<>()
            myLines.put(curTicker, lines)
        }
        lines.add(line)
        parent.getMyPane().getChildren().add(line.view())
    }

    protected void deleteLines(boolean deleteAll,  Map<Stock,List<CanvasGroup>> myLines) {
        List<CanvasGroup> lines = myLines.get(parent.getTicker())

        if (lines == null) {
            log.warn(String.format("No lines for %s",parent.getTicker()))
            return
        }

        List<CanvasGroup> linesToBeRemoved = new ArrayList<>()

        for (CanvasGroup l : lines) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Will attempt to delete line %s with status %d", l, l.getStatus()))
            }
            if (deleteAll || (l.getStatus() == CanvasGroup.SELECTED)) {
                parent.getMyPane().getChildren().remove(l.view())
                linesToBeRemoved.add(l)
            }
        }

        for (CanvasGroup l : linesToBeRemoved) {
            lines.remove(l)
        }
    }

    protected void clearLines(Map<Stock,List<CanvasGroup>> myLines) {
        List<CanvasGroup> lines = myLines.get(parent.getTicker())

        if (lines == null) return

        for (CanvasGroup l : lines) {
            parent.getMyPane().getChildren().remove(l.view())
        }
    }

    protected void refreshLines(Map<Stock,List<CanvasGroup>> myLines) {
        List<CanvasGroup> lines = myLines.get(parent.getTicker())

        if (lines == null) return

        for (CanvasGroup l : lines) {
            parent.getMyPane().getChildren().add(l.view())
        }
    }

    protected void onSaveToMongoDBEvent(SaveToMongoDBEvent event, Map<Stock,List<CanvasGroup>> myLines) {
        Stock stock = parent.getTicker()
        List<CanvasGroup> lines = myLines.get(stock)
        lines.each  { CanvasGroup line ->
            if (line.status != CanvasGroup.NORMAL) {
                MongodbLine mongoLine = (MongodbLine)line
                MongoDBResult result = mongoLine.save(parent)
                if (result.isOk()) {
                    log.info(String.format("(%s) Successfully saved %s with _id: %s to location: %d",
                            stock.getTicker(),
                            mongoLine.whoAmI(),
                            result.getObjectId(),
                            event.getLocation()))
                }
                else {
                    log.error(String.format("(Saving %s %s, %d) %s",
                            mongoLine.whoAmI(),
                            stock.getTicker(),
                            event.getLocation(),
                            result.getWriteResult().getError()))
                }
            }
        }
    }
}
