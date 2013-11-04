package maunaloa.controllers.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import maunaloa.controllers.MongoDBController;
import maunaloa.domain.MaunaloaContext;
import maunaloa.utils.FxUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 30.10.13
 * Time: 11:39
 */
public class MongoDBCommentsController implements MongoDBController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    @FXML
    private Button btnAddComment;
    @FXML
    private Button btnDeleteComment;
    @FXML
    private Button btnClose;
    @FXML
    private TreeView treeComments;
    @FXML
    private TextArea txaComment;

    private MaunaloaContext ctx;

    public void initialize() {
        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FxUtils.closeView(actionEvent);
            }
        });
    }

    @Override
    public void setContext(MaunaloaContext ctx) {
        this.ctx = ctx;
    }
}
