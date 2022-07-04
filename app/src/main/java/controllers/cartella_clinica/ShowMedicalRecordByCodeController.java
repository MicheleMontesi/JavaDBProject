package controllers.cartella_clinica;

import db.ConnectionProvider;
import db.tables.MedicalRecordsTables;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.MedicalRecords;
import utilities.views.CreateMedicalRecordsView;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class ShowMedicalRecordByCodeController implements Initializable {
    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<MedicalRecords> table;
    @FXML
    private TableColumn<MedicalRecords, Integer> medicalIdColumn;
    @FXML
    private TableColumn<MedicalRecords, String> diagnosisColumn, rehabColumn, anamnesisColumn, fiscalCodeColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final MedicalRecordsTables mrTable = new MedicalRecordsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var mr = mrTable.findByCode(toUpperNormalizer(idField));
            if (mr.isPresent()) {
                final ObservableList<MedicalRecords> list = FXCollections.observableArrayList(mr.get());
                CreateMedicalRecordsView.create(table, medicalIdColumn, fiscalCodeColumn, anamnesisColumn, diagnosisColumn,
                        rehabColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The input data doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding idFieldValid = Bindings.createBooleanBinding(() -> idField.getText().isEmpty(), idField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
