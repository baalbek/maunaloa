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
import maunaloa.events.MainFrameControllerListener;
import maunaloa.events.MongoDBControllerListener;
import maunaloa.events.MongoDBEvent;
import maunaloa.models.MaunaloaFacade;
import maunaloa.utils.DateUtils;
import oahu.exceptions.NotImplementedException;
import oahu.financial.Stock;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    private MaunaloaFacade facade;
    private List<MainFrameControllerListener> listeners;
    private Stock stock;
    private int location;

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
                if (stock == null) {
                    System.out.println("Ticker not set");
                    return;
                }
                Date d1 = DateUtils.parse(txFromDate.getText());
                Date d2 = DateUtils.parse(txToDate.getText());


                List<DBObject> lines = facade.getWindowDressingModel().fetchFibonacci(stock.getTicker(), d1, d2);
                for (DBObject line : lines) {
                    System.out.println(line);
                }
                for (MainFrameControllerListener listener : listeners) {
                    listener.onMongoDBEvent(new MongoDBEvent(location,MongoDBEvent.FETCH_FROM_DATASTORE,lines));
                }
            }
        });
    }

    @Override
    public void addListener(MainFrameControllerListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    @Override
    public void setListeners(List<MainFrameControllerListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void setFacade(MaunaloaFacade facade) {
        this.facade = facade;
    }

    @Override
    public void setTicker(Stock stock) {
        this.stock = stock;
    }

    @Override
    public void setLocation(int location) {
        this.location = location;
    }

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
}
