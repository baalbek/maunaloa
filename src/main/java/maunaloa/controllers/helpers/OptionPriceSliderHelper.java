package maunaloa.controllers.helpers;

import maunaloa.views.charts.ChartItem;
import maunaloa.views.charts.OptionPriceSlider;
import oahu.dto.Tuple;
import oahu.financial.Stock;
import oahux.chart.IRuler;
import oahux.controllers.MaunaloaChartViewModel;
import oahux.financial.DerivativeFx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcs on 14.09.15.
 *
 */
public class OptionPriceSliderHelper extends AbstractControllerHelper {
    private List<ChartItem> curLines;
    public OptionPriceSliderHelper(MaunaloaChartViewModel boss) {
        super(boss);
    }

    public void updateSliders(List<DerivativeFx> derivatives) {
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
            OptionPriceSlider slider = new OptionPriceSlider(d, boss.getVruler());
            curLines.add(slider);
            System.out.printf("Adding slider: %s", slider);
        }
        updateMyPaneLines(curLines, lineMap());
    }

    public void updateRuler(IRuler<Double> vruler) {
        lineMap().forEach((Stock key, List<ChartItem> values) -> {
            values.forEach((ChartItem x) -> {
                OptionPriceSlider slider = (OptionPriceSlider)x;
                slider.updateRuler(vruler);
            });
        });
    }
}
