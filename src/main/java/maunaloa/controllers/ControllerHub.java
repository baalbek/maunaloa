package maunaloa.controllers;

import oahu.domain.Tuple;
import oahux.chart.IRuler;
import oahux.controllers.MaunaloaChartViewModel;

/**
 * Created by rcs on 4/24/14.
 */
public interface ControllerHub {
    //Tuple<IRuler> getRulers(int location);
    MaunaloaChartViewModel getViewModel(int location);
}
