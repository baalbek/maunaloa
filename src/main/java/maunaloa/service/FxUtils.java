package maunaloa.service;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 30.10.13
 * Time: 11:55
 */
public class FxUtils {
    public static void loadApp(String fxml,
                               String title,
                               Object controller) {
        try {
            URL url = FxUtils.class.getResource(fxml);

            FXMLLoader loader = new FXMLLoader(url);

            loader.setController(controller);

            Parent parent = (Parent)loader.load();

           /* Object c = loader.getController();
            controllerFn.accept(c);*/

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    public static void loadApp(MaunaloaContext ctx, String fxml, String title) {
        try {
            URL url = MongoDBFetchFibController.class.getResource(fxml);

            FXMLLoader loader = new FXMLLoader(url);

            Parent parent = null;

            parent = (Parent)loader.load();

            MongoDBController c = loader.getController();
            c.setContext(ctx);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //*/
    public static void closeView(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public static void closeView(KeyEvent keyEvent) {
        Node source = (Node) keyEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
