package maunaloa.controllers.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import maunaloa.controllers.DerivativesController;
import oahu.exceptions.NotImplementedException;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/26/13
 * Time: 12:26 AM
 */
public class ChartCanvasController implements DerivativesController {
    @FXML public Pane paneCandlesticks;
    @FXML public VBox containerCandlesticks;
    @FXML public Canvas myCanvas;
    @FXML public VBox cndldialog;
    @FXML TextField txDraw;

    private String heyHey = "Default";

    public void doDraw(ActionEvent event) {
        System.out.println("Trying to draw on " + myCanvas);
        draw();
    }

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

    public String getHeyHey() {
        return heyHey;
    }

    public void setHeyHey(String heyHey) {
        this.heyHey = heyHey;
    }
}
