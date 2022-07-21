package controllers.paziente;

import utilities.ConnectionProvider;
import db.tables.PatientsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Patient;
import utilities.views.CreatePatientView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class PatientsViewController implements Initializable {
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

    private final ObservableList<Patient> list = FXCollections.observableArrayList(patientsTables.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreatePatientView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                patientIdColumn, rulesColumn, privacyColumn, consentColumn, list);
    }
}
