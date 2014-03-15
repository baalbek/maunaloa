package maunaloa.controllers.groovy

import maunaloa.events.DerivativesCalculatedEvent
import maunaloa.events.StockPriceAssignedEvent
import maunaloa.views.CanvasGroup
import maunaloa.views.RiscLines
import oahu.financial.Stock
import oahux.domain.DerivativeFx

/**
 * Created by rcs on 2/24/14.
 */
class RiscLevelsController extends ChartCanvasControllerHelper {
    private Map<Stock,List<CanvasGroup>> levels = new HashMap<>()
    private Stock spot

    void clearLines() {
        clearLines(levels)
    }

    void refreshLines() {
        refreshLines(levels)
    }

    void deleteLines(boolean deleteAll) {
        deleteLines(deleteAll, levels)
    }

    void onDerivativeCalculatedEvent(DerivativesCalculatedEvent event) {
        event.calculated.each { DerivativeFx fx ->
            def level = new RiscLines(fx, parent.vruler)
            levels.add(riscLines)
            updateMyPaneLines(levels,level)
        }
    }

    void onStockPriceAssignedEvent(StockPriceAssignedEvent event) {

    }
}
