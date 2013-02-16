package maunaloa;

import maunaloa.controllers.DerivativesController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class App extends Application {
    public static void main(String[] args) {

        Locale.setDefault(Locale.US);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        initLog4j();

        ApplicationContext factory = new ClassPathXmlApplicationContext("maunaloa.xml");

        URL url = this.getClass().getResource("/MainFrame.fxml");

        FXMLLoader loader = new FXMLLoader(url);

        final DerivativesController controller = factory.getBean("derivatives-controller",DerivativesController.class);

        loader.setController(controller);

        Parent parent = (Parent)loader.load();

        stage.setTitle("Maunaloa!");
        stage.setScene(new Scene(parent));
        stage.show();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.draw();
            }
        });
    }

    private void initLog4j() {
        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/log4j.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PropertyConfigurator.configure(props);
    }
}