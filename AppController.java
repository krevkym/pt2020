
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Trida, ktera obsluhuje hlavni okno aplikace
 * @author Makek Křevký a Robin Kříž
 */
public class AppController extends Thread {
    /**
     * Tlacitko pro nacteni souboru
     */
    @FXML
    private Button fileCh;
    /**
     * MenuItem pro nacteni ze souboru
     */
    @FXML
    private MenuItem mLoad;
    /**
     * MenuItem pro zapnuti simulace
     */
    @FXML
    private MenuItem mSim;
    /**
     * Hlavni stage aplikace
     */
    private Stage mainStage;
    /**
     * Soubor pro simulaci
     */
    private File file;
    /**
     * Tlacitko pro zapnuti simulace
     */
    @FXML
    public Button start;
    /**
     * TextArea pro vypis celkove ceny simulace
     */
    @FXML
    public TextArea sum;
    /**
     * TextArea pro vypis ceny za den
     */
    @FXML
    public TextArea daily;
    /**
     * TextArea pro vypis dne simulace
     */
    @FXML
    public TextArea whatDay;
    /**
     * TextField pro vypis cesty k souboru
     */
    @FXML
    public TextField fileTxt;
    /**
     * TextArea pro vypis prubehu simulace
     */
    @FXML
    public TextArea outcome;
    /**
     * Atribut tridy pro simulaci programu
     */
    private Simulation s;
    /**
     * Atribut tridy pro praci se souborem
     */
    private FileIO f;

    /**
     * Simulace programu
     */
    public  void simulate() {
        if(f != null) {
            try {
                f.loadFromFile(file);
                outcome.setText("");
            } catch (NumberFormatException nfe) {
                outcome.setText("Nepodařilo se správně načíst soubor. Ujistěte se, že je ve správném formátu.");
                return;
            }
            start.setDisable(true);
            fileCh.setDisable(true);
            mSim.setDisable(true);
            mLoad.setDisable(true);
            s = new Simulation(f, this);
            System.out.println("Celková cena: " + s.simulate());
            start.setDisable(false);
            fileCh.setDisable(false);
            mSim.setDisable(false);
            mLoad.setDisable(false);
        } else {
            outcome.setText("Nebyl načten žádný soubor");
        }
    }

    /**
     * Metoda pro obsluhu akce na zapnuti simulace
     */
    public void simulateAction(){
        Thread thread = new Thread(this::simulate);
        thread.start();
    }

    /**
     * Pozastaveni simulace
     */
    public void pauseSimulation() {
        if(s != null) {
            s.setPaused(true);
        }
    }

    /**
     * Pokracovani simulace
     */
    public void continueSimulation() {
        if(s != null) {
            s.setPaused(false);
        }
    }

    /**
     * Stopnuti simulace
     */
    public void stopSimulation() {
        if(s != null) {
            continueSimulation();
            s.setStop(true);
        }
    }

    /**
     * Vypnuti aplikace
     */
    public void exitApp() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Metoda pro obsluhu akce na nacteni souboru
     */
    public void loadFile() {
        File fileTMP = file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        file = fileChooser.showOpenDialog(mainStage);
        if(file == null) {
            if(f == null) {
                outcome.setText("Nebyl vybran zadny soubor");
            } else {
                outcome.setText("Nevybrali jste nový soubor. Používám naposledy načtený.");
                file = fileTMP;
            }
        } else {
            fileTxt.setText(file.getPath());
            outcome.setText("");
            f = new FileIO();
        }
    }

    /**
     * Metoda pro obsluhu akce na generovani dat
     * @throws IOException vyjimka
     */
    public void generate() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/generator.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Generátor");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Getter
     * @return atribut TextArea sum
     */
    public TextArea getSum() {
        return sum;
    }

    /**
     * Getter
     * @return atribut TextArea daily
     */
    public TextArea getDaily() {
        return daily;
    }

    /**
     * Getter
     * @return atribut TextArea whatDay
     */
    public TextArea getWhatDay() {
        return whatDay;
    }

    /**
     * Getter
     * @return atribut TextArea outcome
     */
    public TextArea getOutcome() {
        return outcome;
    }

    /**
     * Setter - nastavi atribut stage
     * @param stage nova Stage
     */
    public void setStage(Stage stage) {
        mainStage = stage;
    }
}
