package semestralka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 720, 579));
        primaryStage.setMinHeight(579);
        primaryStage.setMinWidth(720);
        primaryStage.show();
        Controller myController = loader.getController();
        myController.setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}