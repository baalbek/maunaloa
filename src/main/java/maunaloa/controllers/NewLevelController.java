package maunaloa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import maunaloa.service.FxUtils;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 21.11.13
 * Time: 14:07
 */
public class NewLevelController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOk;
    @FXML
    private TextField txLevel;

    public void initialize() {
        btnCancel.setOnAction(event -> {
            FxUtils.closeView(event);
        });
        btnOk.setOnAction(event -> {
            fireNewLevelEvent();
        });
        txLevel.setOnAction(event -> {
            fireNewLevelEvent();
            FxUtils.closeView(event);
        });
    }

    private void fireNewLevelEvent() {
        /*
        NewLevelEvent evt =
                new NewLevelEvent(
                        ctx.getLocation(),
                        Double.parseDouble(txLevel.getText()));

        for (MainFrameControllerListener listener : ctx.getListeners()) {
            listener.onNewLevelEvent(evt);
        }
        //*/
    }
}
