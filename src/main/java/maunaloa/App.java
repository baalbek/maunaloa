package maunaloa;

import maunaloa.controllers.DerivativesController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maunaloa.controllers.impl.CndlController;
import maunaloa.controllers.impl.DerivativesControllerImpl2;
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



        ApplicationContext factory = new ClassPathXmlApplicationContext("maunaloa2.xml");

        URL url = this.getClass().getResource("/MainFrame2.fxml");

        FXMLLoader loader = new FXMLLoader(url);

        final DerivativesController controller = factory.getBean("derivatives-controller",DerivativesController.class);

        loader.setController(controller);




        System.out.println("This one before? " + ((DerivativesControllerImpl2)controller).derivativesTableView);

        Parent parent = (Parent)loader.load();

        System.out.println("This one after? " + ((DerivativesControllerImpl2)controller).derivativesTableView);

        CndlController cndlController = new CndlController();

        ssetController("/Candlesticks.fxml", cndlController);

        ((DerivativesControllerImpl2)controller).cndlController = cndlController;

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



    private void ssetController(String fxmlFile, DerivativesController controller)
    {
        try {
            URL url = this.getClass().getResource(fxmlFile);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);

            loader.load();

            System.out.println("Now? " + ((CndlController)controller).paneCandlesticks);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Object loadController(String fxmlFile) throws IOException
    {
        /*
        InputStream fxmlStream = null;
        try
        {
            fxmlStream = getClass().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            return loader.getController();
        }
        finally
        {
            if (fxmlStream != null)
            {
                fxmlStream.close();
            }
        }
        */
        URL url = this.getClass().getResource(fxmlFile);
        FXMLLoader loader = new FXMLLoader(url);
        loader.load();
        return loader.getController();
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