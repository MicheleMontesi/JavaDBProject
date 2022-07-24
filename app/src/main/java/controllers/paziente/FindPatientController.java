package controllers.paziente;

import db.tables.PatientsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Patient;
import utilities.ConnectionProvider;
import utilities.views.CreatePatientView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class FindPatientController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;
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

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(idBox)) {
            var patient = patientsTables.findByCode(idBox.getValue());
            if (patient.isPresent()) {
                final ObservableList<Patient> list = FXCollections.observableArrayList(patient.get());
                CreatePatientView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                        patientIdColumn, rulesColumn, privacyColumn, consentColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idBox.getValue() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, patientsTables, e -> e.getId().get(0));
    }
}
