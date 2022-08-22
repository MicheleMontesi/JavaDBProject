package controllers.ospitazione;

import db.tables.PatientsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Patient;
import utilities.ConnectionProvider;
import utilities.views.CreatePatientView;

import java.util.Date;

import static utilities.checkers.PersonCheckers.intCheck;

public class ListPatientsPerYearHosting {

    @FXML
    private TableView<Patient> table;
    @FXML
    private TextField yearField;
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
        if (intCheck(yearField, 4, 4)) {
            var patient = patientsTables.findPatientByYear(
                    Integer.parseInt(yearField.getText()));
            if (patient.isPresent()) {
                final ObservableList<Patient> patientList = FXCollections.observableArrayList(patient.get());
                CreatePatientView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                        patientIdColumn, rulesColumn, privacyColumn, consentColumn, patientList);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The year selected isn´t related to any of the patients");
                errorAlert.showAndWait();
            }
        }
    }
}
