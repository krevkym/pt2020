
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * Hlavni trida aplikace
 * @author Marek Křevký a Robin Kříž
 */
public class Main extends Application {

    /**
     * Startovaci metoda
     * @param primaryStage hlavni stage programu
     * @throws Exception vyjimka
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        File f = new File("fxml/sample.fxml");
        System.out.println(f.getPath() + " " + f.exists());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Aplikace");
        primaryStage.setScene(new Scene(root, 720, 579));
        primaryStage.setMinHeight(579);
        primaryStage.setMinWidth(720);
        primaryStage.show();
        AppController myAppController = loader.getController();
        primaryStage.setOnCloseRequest(event -> myAppController.exitApp());
        myAppController.setStage(primaryStage);
    }

    /**
     * Spousteci metoda aplikace
     * @param args argumenty programu
     */
    public static void main(String[] args) {
        launch(args);
    }
}