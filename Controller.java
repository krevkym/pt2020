package semestralka;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller extends Thread {

    private Stage mainStage;
    private File file;

    public TextArea getSumm() {
        return summ;
    }
    public TextArea getDaily() {
        return daily;
    }
    public TextArea getWhatDay() {
        return whatDay;
    }

    @FXML
    public Button start;
    @FXML
    public TextArea summ;
    @FXML
    public TextArea daily;
    @FXML
    public TextArea whatDay;
    @FXML
    public TextField fileTxt;
    @FXML
    public TextArea outcome;


    private Simulation s;
    private FileIO f;

    @Override
    public void run() {
        System.out.println("Inside : " + Thread.currentThread().getName());
    }


    public  void simulace() {
        if(f != null) {
            f.loadFromFile(file);
            outcome.setText("");
        } else {
            outcome.setText("Nebyl nacten zadny soubor");
            return;
        }
        start.setDisable(true);
        s = new Simulation(f, this);
        System.out.println("Celkova cena: " + s.simulate());
        start.setDisable(false);
    }

    public void simulateAction(){
        Thread thread = new Thread(this::simulace);
        thread.start();
    }

    public void pauseSimulation() {
        if(s != null) {
            s.setPaused(true);
        }
    }

    public void continueSimulation() {
        if(s != null) {
            s.setPaused(false);
        }
    }

    public void stopSimulation() {
        if(s != null) {
            continueSimulation();
            s.stop = true;
        }
    }

    public void loadFile() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(mainStage);
        if(file == null) {
            outcome.setText("Nebyl vybran zadny soubor");
            return;
        }
        fileTxt.setText(file.getPath());
        outcome.setText("");
        f = new FileIO();
    }

    public TextArea getOutcome() {
        return outcome;
    }
    public void setStage(Stage stage) {
        mainStage = stage;
    }

}
