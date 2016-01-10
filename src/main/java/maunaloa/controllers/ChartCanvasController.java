package maunaloa.controllers;

import oahux.chart.MaunaloaChart;
import oahux.controllers.ControllerCategory;

import java.time.LocalDate;

/**
 * Created by rcs on 30.11.15.
 *
 */
public class ChartCanvasController {


    //region Properties
    private ControllerCategory controllerCategory;
    private MainframeController mainframeController;
    private MaunaloaChart chart;

    public void setMainframeController(MainframeController mainframeController) {
        this.mainframeController = mainframeController;
    }

    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
    }

    public void setControllerCategory(ControllerCategory controllerCategory) {
        this.controllerCategory = controllerCategory;
    }
    LocalDate getLastCurrentDateShown() {
        return chart.getLastCurrentDateShown();
    }
    //endregion Properties
}
