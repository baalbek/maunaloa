package maunaloa.controllers.impl;

import com.mongodb.DBObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import maunaloa.controllers.MongoDBController;
import maunaloa.domain.MaunaloaContext;
import maunaloa.events.mongodb.FetchFromMongoDBEvent;
import maunaloa.utils.DateUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 10/5/13
 * Time: 3:58 PM
 */
public class MongoDBControllerImpl implements MongoDBController {
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
                // close the dialog.
                closeView(actionEvent);
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
                closeView(actionEvent);
            }
        });
    }

    private void closeView(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setContext(MaunaloaContext ctx) {
        this.ctx = ctx;
    }

    public static void loadApp(MaunaloaContext ctx) {
        try {
            URL url = MongoDBControllerImpl.class.getResource("/FetchFromMongoDialog.fxml");

            FXMLLoader loader = new FXMLLoader(url);

            Parent parent = null;

            parent = (Parent)loader.load();

            MongoDBController c = loader.getController();
            c.setContext(ctx);

            Stage stage = new Stage();
            stage.setTitle("Fetch from MongoDB");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    public static void loadApp(Stock stock,
                               MaunaloaFacade facade,
                               List<MainFrameControllerListener> listeners,
                               int location) {
        try {
            URL url = MongoDBControllerImpl.class.getResource("/FetchFromMongoDialog.fxml");

            FXMLLoader loader = new FXMLLoader(url);

            Parent parent = null;

            parent = (Parent)loader.load();

            MongoDBController c = loader.getController();
            c.setListeners(listeners);
            c.setFacade(facade);
            c.setTicker(stock);
            c.setLocation(location);

            Stage stage = new Stage();
            stage.setTitle("Fetch from MongoDB");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //*/
}
