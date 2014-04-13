package maunaloa.controllers;

import oahux.chart.MaunaloaChart;

/**
 * Created by rcs on 4/13/14.
 */
public class ChartCanvasController {
    private MaunaloaChart chart;
    private String name;
    private int location;

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(int location) {
        this.location = location;

    }
    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
    }
}
