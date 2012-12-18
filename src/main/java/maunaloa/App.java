package maunaloa;

import maunaloa.controllers.DerivativesController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.net.URL;
import java.util.Locale;

public class App extends Application {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("/maunaloa.xml"));

        URL url = this.getClass().getResource("/MainFrame.fxml");

        FXMLLoader loader = new FXMLLoader(url);

        final DerivativesController controller = (DerivativesController)factory.getBean("derivatives-controller");

        loader.setController(controller);

        Parent parent = (Parent)loader.load();

        stage.setTitle("FXML Welcome");
        stage.setScene(new Scene(parent));
        stage.show();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.draw();
            }
        });
    }
}