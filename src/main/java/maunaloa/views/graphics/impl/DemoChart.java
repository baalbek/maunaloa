package maunaloa.views.graphics.impl;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import oahu.controllers.ChartViewModel;
import oahu.views.MaunaloaChart;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 11/20/12
 * Time: 11:18 PM
 */
public class DemoChart implements MaunaloaChart {
    private ChartViewModel viewModel;

    @Override
    public void draw(Canvas canvas) {
        double w = canvas.getWidth();

        double h = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        if ((w > 0.0) && (h > 0.0)) {
            System.out.println("Graphics Context: " + gc + ", w: " + w + ", h: " + h);

            gc.clearRect(0,0, w, h);
            //gc.setStroke(Color.rgb(95,68,34,0.4));
            gc.setFill(Color.WHITE);
            gc.fillRect(0,0,w,h);
            //gc.setStroke(Color.rgb(255,255,255));

            gc.setStroke(Color.rgb(0,0,0));


            double x0 = 10.5;
            double x1 = w - x0;
            double y0 = 10.5;
            double y1 = h - y0;


            /*
            drawLine2(gc, x0, y0, x1, y0);
            drawLine2(gc, x1, y0, x1, y1);
            drawLine2(gc, x1, y1, x0, y1);
            drawLine2(gc, x0, y1, x0, y0);
            */
            gc.setLineWidth(0.25);
            drawLine(gc, x0+10, y0+10, x1-10, y0+10);
            gc.setLineWidth(0.5);
            drawLine(gc, x1-10, y0+10, x1-10, y1-10);
            gc.setLineWidth(0.75);
            drawLine(gc, x1-10, y1-10, x0+10, y1-10);
            gc.setLineWidth(5.25);
            drawLine(gc, x0+10, y1-10, x0+10, y0+10);
        }
    }

    @Override
    public void setViewModel(ChartViewModel viewModel) {
        this.viewModel = viewModel;
    }


    private void drawLine(GraphicsContext gc, double x0, double y0, double x1, double y1) {
        gc.beginPath();
        gc.moveTo(x0,y0);
        gc.lineTo(x1,y1);
        gc.closePath();
        gc.stroke();
    }

    private void drawLine2(GraphicsContext gc, double x0, double y0, double x1, double y1) {
        gc.strokeLine(x0,y0,x1,y1);
    }
}
