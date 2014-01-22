package maunaloa.controllers.groovy

import maunaloa.controllers.ChartCanvasController
import maunaloa.views.CanvasGroup
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
}
