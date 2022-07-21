package controllers.assumere_terapia;

import db.tables.PatientsTables;
import db.tables.TakeTherapiesTables;
import db.tables.TherapiesTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import model.Patient;
import model.TakeTherapy;
import model.Therapy;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class CreateTakeTherapyController implements Initializable {
    @FXML
    private ChoiceBox<String> fiscalCodeBox, therapyIdBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final TakeTherapiesTables ttTable = new TakeTherapiesTables(connectionProvider.getMySQLConnection());
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());
    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (check()) {
            final var fiscalCode = fiscalCodeBox.getValue();
            final var therapyId = Integer.parseInt(therapyIdBox.getValue());

            ttTable.save(new TakeTherapy(fiscalCode, therapyId));
        }
    }

    private boolean check() {
        return choiceBoxChecker(fiscalCodeBox) &
                choiceBoxChecker(therapyIdBox) &
                !ttAlreadyExists();
    }

    private boolean ttAlreadyExists() {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input already exists");
        for (var patient : ttTable.findAll().stream().map(TakeTherapy::fiscalCode).toList()) {
            if (patient.equals(fiscalCodeBox.getValue())) {
                for (var therapy : ttTable.findAll().stream().map(TakeTherapy::therapyId).map(Objects::toString).toList()) {
                    if (therapy.equals(therapyIdBox.getValue())) {
                        errorAlert.showAndWait();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (fiscalCodeBox != null) {
            fiscalCodeBox.getItems().addAll(patientsTables.findAll().stream().map(Patient::fiscalCode).toList());
        }
        if (therapyIdBox != null) {
            therapyIdBox.getItems().addAll(therapiesTable.findAll().stream().map(Therapy::therapyId).map(Objects::toString).toList());
        }
    }
}
