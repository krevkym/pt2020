
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Trida, ktera obsluhuje akce pro okno na generaci dat
 * @author Marek Křevký a Robin Kříž
 */
public class GeneratorController {
    /**
     * Spinner na pocet tovaren
     */
    @FXML
    private Spinner<Integer> facCount;
    /**
     * Spinner na pocet supermarketu
     */
    @FXML
    private Spinner<Integer> supCount;
    /**
     * Spinner na pocet druhu zbozi
     */
    @FXML
    private Spinner<Integer> goodsCount;
    /**
     * Spinner na pocet dni simulace
     */
    @FXML
    private Spinner<Integer> daysCount;
    /**
     * Spinner na minimalni cenu prevozu
     */
    @FXML
    private Spinner<Integer> minPrice;
    /**
     * Spinner na maximalni cenu prevozu
     */
    @FXML
    private Spinner<Integer> maxPrice;
    /**
     * Spinner na minimalni pocet pocatecnich zasob zbozi
     */
    @FXML
    private Spinner<Integer> minSupplies;
    /**
     * Spinner na maximalni pocet pocatecnich zasob zbozi
     */
    @FXML
    private Spinner<Integer> maxSupplies;
    /**
     * Spinner na minimalni produkci tovaren
     */
    @FXML
    private Spinner<Integer> minProd;
    /**
     * Spinner na maximalni produkci tovaren
     */
    @FXML
    private Spinner<Integer> maxProd;
    /**
     * Spinner na minimalni poptavku supermarketu
     */
    @FXML
    private Spinner<Integer> minDem;
    /**
     * Spinner na maximalni poptavku supermarketu
     */
    @FXML
    private Spinner<Integer> maxDem;

    /**
     * Inicializacni metoda tridy
     */
    public void initialize() {
        minPrice.valueProperty().addListener((obs, oldValue, newValue) -> maxPrice.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(newValue, 100, maxPrice.getValue())));
        minSupplies.valueProperty().addListener((obs, oldValue, newValue) -> maxSupplies.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(newValue, 100, maxSupplies.getValue())));
        minProd.valueProperty().addListener((obs, oldValue, newValue) -> maxProd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(newValue, 100, maxProd.getValue())));
        minDem.valueProperty().addListener((obs, oldValue, newValue) -> maxDem.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(newValue, 100, maxDem.getValue())));
    }

    /**
     * Metoda, která zavre okno pro generaci
     * @param actionEvent Action event
     */
    public void exitGen(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Metoda, ktera zapne generaci souboru
     * @param actionEvent Action event
     */
    public void generate(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File("./"));
        chooser.setInitialFileName("generated.txt");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File saveAs = chooser.showSaveDialog(stage);
        int[] data = new int[] {facCount.getValue(), supCount.getValue(), goodsCount.getValue(), daysCount.getValue(), minPrice.getValue(), maxPrice.getValue(), minSupplies.getValue(),
        maxSupplies.getValue(), minProd.getValue(), maxProd.getValue(), minDem.getValue(), maxDem.getValue()};
        FileIO fio = new FileIO();
        fio.generateData(saveAs, data);
        stage.close();
    }
}
