package maunaloa.controllers.impl;

import com.mongodb.DBObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import maunaloa.controllers.MongoDBController;
import maunaloa.events.MongoDBControllerListener;
import maunaloa.models.MaunaloaFacade;
import maunaloa.utils.DateUtils;

import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/5/13
 * Time: 3:58 PM
 */
public class MongoDBControllerImpl implements MongoDBController {
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOk;
    @FXML
    private TextField txFromDate;
    @FXML
    private TextField txToDate;
    private MongoDBControllerListener listener;
    private MaunaloaFacade facade;

    public void initialize() {
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // close the dialog.
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Date d1 = DateUtils.parse(txFromDate.getText());
                Date d2 = DateUtils.parse(txToDate.getText());
                List<DBObject> lines = facade.getWindowDressingModel().fetchFibonacci("OSEBX", d1, d2);
                for (DBObject line : lines) {
                    System.out.println(line);
                }
            }
        });
    }

    @Override
    public void setListener(MongoDBControllerListener listener) {
        this.listener = listener;
    }

    @Override
    public void setFacade(MaunaloaFacade facade) {
        this.facade = facade;
    }
}
