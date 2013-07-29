package maunaloa.controllers.impl;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import maunaloa.controllers.ChartCanvasController;
import oahux.chart.MaunaloaChart;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/26/13
 * Time: 12:26 AM
 */
public class DefaultChartCanvasController implements ChartCanvasController {
    @FXML private Canvas myCanvas;

    private MaunaloaChart chart;

    //region Fibonacci

    //endregion Fibonacci

    //region Interface methods

    @Override
    public void draw() {

        System.out.println("myCanvas: " + myCanvas +
                            ", h: " + myCanvas.getHeight() +
                            ", w: " + myCanvas.getWidth() +
                            ", x: " + myCanvas.getTranslateX() +
                            ", y: " + myCanvas.getTranslateY());
        GraphicsContext ctx = myCanvas.getGraphicsContext2D();

        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.lineTo(1200, 1200);
        ctx.closePath();
        ctx.setStroke(Color.BLACK);
        ctx.stroke();
    }

    @Override
    public void setChart(MaunaloaChart chart) {
        this.chart = chart;
    }
    //endregion  Interface methods
}
