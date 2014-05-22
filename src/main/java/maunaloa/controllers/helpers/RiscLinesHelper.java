package maunaloa.controllers.helpers;

import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.RiscLines;
import oahux.controllers.MaunaloaChartViewModel;
import oahux.financial.DerivativeFx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcs on 5/20/14.
 */
public class RiscLinesHelper extends AbstractControllerHelper {
    private List<ChartItem> curLines;

    public RiscLinesHelper(MaunaloaChartViewModel boss) {
        super(boss);
    }

    public void updateRiscs(List<DerivativeFx> derivatives) {
        if (curLines == null) {
            curLines = new ArrayList<>();
        }
        else {
            for (ChartItem item : curLines) {
                item.removeFrom(boss.getPane().getChildren());
            }
            curLines.clear();
        }

        for (DerivativeFx d : derivatives) {
            RiscLines riscLines = new RiscLines(d, boss.getVruler());
            curLines.add(riscLines);
        }
        updateMyPaneLines(curLines, lineMap());
    }
}
