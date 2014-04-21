package maunaloa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maunaloa.controllers.MainframeController;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class App extends Application {

    private static String springXmlFileName = "maunaloa.xml";


    /*
    public static void main(String[] args) {
        java.util.List<Integer> list = java.util.Arrays.asList(1,2,3,4,5,6,7);
        list.stream().map((x) -> x*x).forEach(System.out::println);

        int sum = list.stream().map(x -> x*x).reduce((x,y) -> x + y).get();
        System.out.println(sum);
    }
    */

    //*
    public static void xmain(String[] args) {

        Locale.setDefault(Locale.US);

        if (args.length > 0) {
            springXmlFileName = args[0];
        }

        System.out.println("Xml file: " + springXmlFileName);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        initLog4j();

        ApplicationContext factory = new ClassPathXmlApplicationContext(springXmlFileName);

        URL url = this.getClass().getResource("/MainFrame.fxml");

        FXMLLoader loader = new FXMLLoader(url);

        final MainframeController controller = factory.getBean("mainframe-controller",MainframeController.class);
        //controller.setSqldbUrl(getSqldbUrl());

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
    private static String getSqldbUrl() {
        Properties props = new Properties();
        try {
            props.load(App.class.getClassLoader().getResourceAsStream("dbcp.properties"));
            String dburl = props.getProperty("db.url");
            String[] dburlSplit = dburl.split("://");
            return dburlSplit[1];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //*/
}
