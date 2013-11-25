package maunaloa.controllers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import maunaloa.controllers.MongoDBController;
import maunaloa.domain.MaunaloaContext;
import maunaloa.events.MainFrameControllerListener;
import maunaloa.events.NewLevelEvent;
import maunaloa.utils.FxUtils;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 21.11.13
 * Time: 14:07
 */
public class NewLevelController implements MongoDBController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOk;
    @FXML
    private TextField txLevel;

    private MaunaloaContext ctx;

    public void initialize() {
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FxUtils.closeView(actionEvent);
            }
        });
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                NewLevelEvent evt =
                    new NewLevelEvent(
                        ctx.getLocation(),
                        Double.parseDouble(txLevel.getText()));

                for (MainFrameControllerListener listener : ctx.getListeners()) {
                    listener.onNewLevelEvent(evt);
                }
                FxUtils.closeView(actionEvent);
            }
        });
    }

    @Override
    public void setContext(MaunaloaContext ctx) {
        this.ctx = ctx;
    }
}
