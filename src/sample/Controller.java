package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.*;
import javafx.scene.layout.VBox;

public class Controller extends Thread  implements Initializable {
    int q = 0;
    String text = "";

    @FXML
    public ComboBox<String> combo;



    @FXML
    public Button start;

    @FXML
    public TextArea summ;



    @FXML
    public TextArea daily;

    @FXML
    public TextArea whatDay;

    @Override
    public void run() {
        System.out.println("Inside : " + Thread.currentThread().getName());
    }



    public static ObservableList<String> list = FXCollections.observableArrayList("real_large.txt", "real_medium.txt", "real_small.txt", "real_small_sink.txt", "test_optim.txt", "test_optim_sink.txt", "test_price.txt", "test_small.txt");

    @Override
    public void initialize(URL location, ResourceBundle resources){
        combo.setItems(list);

        

    }


    public  void simulace() {
        String[] filenames = {"real_large.txt", "real_medium.txt", "real_small.txt", "real_small_sink.txt", "test_optim.txt", "test_optim_sink.txt", "test_price.txt", "test_small.txt"};
        String[] soubor = new String[list.size()];
        for (int i = 1; i < list.size(); i++) {
            soubor[i - 1] = filenames[i - 1];
        }


        q = combo.getSelectionModel().getSelectedIndex();
        int a = Integer.parseInt(String.valueOf(q));
        System.out.println(q);

        FileIO f = new FileIO();
        f.loadFromFile(filenames[a]);


            Simulation s = new Simulation(f);
            System.out.println("Celkova cena: " + s.simulate());


        summ.setText(String.valueOf(s.all));
        daily.clear();
        daily.setText(String.valueOf(s.perDay));
        whatDay.clear();
        whatDay.setText(String.valueOf(s.den));







    }



    public void handle(ActionEvent event){
        Thread thread = new Thread(()-> {
            simulace();

        });
       thread.start();
    }


}
