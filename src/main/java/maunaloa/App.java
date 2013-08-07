package maunaloa;

import maunaloa.controllers.DerivativesController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maunaloa.controllers.MainFrameController;
import oahux.models.MaunaloaFacade;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import javafx.scene.image.Image;

public class App extends Application {
    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        /*
        ApplicationContext factory = new ClassPathXmlApplicationContext("maunaloa.xml");

        MaunaloaFacade facade = factory.getBean("facade",MaunaloaFacade.class);

        java.util.Collection<oahu.financial.Derivative> calls = facade.calls("YAR");

        for (oahu.financial.Derivative d : calls) {
            System.out.println(String.format("%s",d.getTicker()));
        }
        //*/

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        initLog4j();

        //stage.getIcons().setAll(new Image(getClass().getResourceAsStream( "/cmd.ico" ))); 
        stage.getIcons().add(new Image(App.class.getResource( "/cmd.png" ).toExternalForm(), 48, 48, false, true)); 
        // stage.getIcons().setAll(new Image("file://tmp100/msdos/cmd.png")); 
        /*
        stage.getIcons().add(
                new Image(
                    JavaFXWindow.class.getResource(
                        "ui/resources/appicon_48x48.png").toExternalForm(), 48, 48, false, true));
        */ 
        ApplicationContext factory = new ClassPathXmlApplicationContext("maunaloa.xml");

        URL url = this.getClass().getResource("/MainFrame.fxml");

        FXMLLoader loader = new FXMLLoader(url);

        final MainFrameController controller = factory.getBean("mainframe-controller",MainFrameController.class);

        loader.setController(controller);

        Parent parent = (Parent)loader.load();

        stage.setTitle("Maunaloa!");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private void initLog4j() {
        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/log4j.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PropertyConfigurator.configure(props);
    }
}
