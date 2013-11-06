package maunaloa.controllers.impl;

import com.mongodb.DBObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import maunaloa.controllers.MongoDBController;
import maunaloa.domain.MaunaloaContext;
import maunaloa.events.mongodb.FetchFromMongoDBEvent;
import maunaloa.utils.DateUtils;
import maunaloa.utils.FxUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/5/13
 * Time: 3:58 PM
 */
public class MongoDBFetchFibController implements MongoDBController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOk;
    @FXML
    private TextField txFromDate;
    @FXML
    private TextField txToDate;

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
                if (ctx.getStock() == null) {
                    System.out.println("Ticker not set");
                    return;
                }

                Date d1 = null;

                if (txFromDate.getText().isEmpty()) {
                    d1 = ctx.getStartDate();
                }
                else {
                    d1 = DateUtils.parse(txFromDate.getText());
                }

                Date d2 = null;
                if (txToDate.getText().isEmpty()) {
                    d2 = ctx.getEndDate();
                }
                else {
                    d2 = DateUtils.parse(txToDate.getText());
                }


                List<DBObject> lines = ctx.getFacade().getWindowDressingModel().fetchFibonacci(
                        ctx.getStock().getTicker(),
                        ctx.getLocation(),
                        d1,
                        d2);

                log.info(String.format("Fetched %d lines for ticker %s, location: %d between %s and %s",
                        lines.size(),
                        ctx.getStock().getTicker(),
                        ctx.getLocation(),
                        d1,
                        d2));
                ctx.getListener().onFetchFromMongoDBEvent(new FetchFromMongoDBEvent(lines));
                FxUtils.closeView(actionEvent);
            }
        });
    }


    @Override
    public void setContext(MaunaloaContext ctx) {
        this.ctx = ctx;
    }
}
