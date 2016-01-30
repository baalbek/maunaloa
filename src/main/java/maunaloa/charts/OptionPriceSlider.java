package maunaloa.charts;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import oahu.financial.OptionCalculator;
import oahux.chart.IRuler;
import oahux.financial.DerivativeFx;
import oahux.repository.ColorRepository;

import java.time.LocalDate;

/**
 * Created by rcs on 12.09.15.
 *
 */


public class OptionPriceSlider implements ChartItem {
    private final LevelLine levelLine;
    private final DerivativeFx derivative;

    public OptionPriceSlider(DerivativeFx derivative, IRuler<Double> ruler, ColorRepository colorRepos) {
        this.derivative = derivative;
        this.levelLine = LevelLine.ofValue(derivative.getStockPrice().getValue(), ruler, colorRepos);
        this.levelLine.setValueLabelFunc((levelValue) -> {
            OptionCalculator calculator = derivative.getCalculator();
            double strike = derivative.getDerivative().getX();
            double t =  derivative.getDays() / 365.0;
            double ivBuy = derivative.getIvBuy();
            //double ivSell = derivative.getIvSell();
            double buy = derivative.getDerivative().getOpType() == 1 ?
                calculator.callPrice(levelValue,strike,t,ivBuy) :
                calculator.putPrice(levelValue,strike,t,ivBuy);
            /*
            double sell = derivative.getDerivative().getOpType() == 1 ?
                    calculator.callPrice(levelValue,strike,t,ivSell) :
                    calculator.putPrice(levelValue,strike,t,ivSell);
                    //*/
            return String.format("Option price buy (%.4f): %.2f kr, level: %.2f kr, Option: %s",
                    ivBuy,
                    buy,
                    levelValue,
                    derivative.getTicker());
        });
    }

    public void updateRuler(IRuler ruler) {
        if (levelLine != null) {
            levelLine.updateRuler(ruler);
        }
    }

    @Override
    public Node view() {
        return levelLine.view();
    }

    @Override
    public void updateRulers(IRuler<LocalDate> hruler, IRuler<Double> vruler) {

    }

    @Override
    public void removeFrom(ObservableList<Node> container) {

    }

}
