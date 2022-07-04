package controllers.assumere_terapia;

import db.ConnectionProvider;
import db.tables.TakeTherapiesTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.TakeTherapy;
import utilities.FXUtils;
import utilities.views.CreateTakeTherapyView;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class TakeTherapySearchCodeController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<TakeTherapy> table;
    @FXML
    private TableColumn<TakeTherapy, String> fiscalCodeColumn;
    @FXML
    private TableColumn<TakeTherapy, Integer> therapyColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final TakeTherapiesTables ttTable = new TakeTherapiesTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var takeTherapy = ttTable.findByCode(toUpperNormalizer(idField));
            if (takeTherapy.isPresent()) {
                final ObservableList<TakeTherapy> list = FXCollections.observableArrayList(takeTherapy.get());
                CreateTakeTherapyView.create(table, fiscalCodeColumn, therapyColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idField.getText() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXUtils.disableOnWrite(searchButton, idField);
    }
}
