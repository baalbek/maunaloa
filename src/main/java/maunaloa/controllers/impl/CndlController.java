package maunaloa.controllers.impl;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import maunaloa.controllers.DerivativesController;
import oahu.exceptions.NotImplementedException;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 7/26/13
 * Time: 12:26 AM
 */
public class CndlController implements DerivativesController {
    @FXML public Pane paneCandlesticks;
    @FXML public VBox containerCandlesticks;
    @FXML public Canvas myCanvas;
    @FXML public VBox cndldialog;

    @Override
    public void draw() {
        throw new NotImplementedException();
    }
}
