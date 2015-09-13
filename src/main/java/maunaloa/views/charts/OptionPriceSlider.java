package maunaloa.views.charts;

import oahu.financial.Derivative;
import oahux.chart.IRuler;

/**
 * Created by rcs on 12.09.15.
 *
 */
public class OptionPriceSlider extends LevelLine {
    private final Derivative derivative;
    public OptionPriceSlider(double levelValue, IRuler ruler) {
        super(levelValue, ruler);
        derivative = null;
    }
    public OptionPriceSlider(Derivative derivative, double levelValue, IRuler ruler) {
        super(levelValue, ruler);
        this.derivative = derivative;
    }

    @Override
    protected String valueLabelText() {
        return null;
    }
}
