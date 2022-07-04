package controllers.paziente;

import db.ConnectionProvider;
import db.tables.PatientsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Patient;
import utilities.views.CreatePatientView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FXUtils.disableOnWrite;
import static utilities.checkers.PersonCheckers.lengthChecker;

public class FindPatientController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Patient> table;
    @FXML
    private TableColumn<Patient, String> idColumn, nameColumn, surnameColumn, residenceColumn, genderColumn;
    @FXML
    private TableColumn<Patient, Date> birthColumn;
    @FXML
    private TableColumn<Patient, Integer> patientIdColumn;
    @FXML
    private TableColumn<Patient, Boolean> rulesColumn, consentColumn, privacyColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var patient = patientsTables.findByCode(idField.getText());
            if (patient.isPresent()) {
                final ObservableList<Patient> list = FXCollections.observableArrayList(patient.get());
                CreatePatientView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                        patientIdColumn, rulesColumn, privacyColumn, consentColumn, list);
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
        disableOnWrite(searchButton, idField);
    }
}
