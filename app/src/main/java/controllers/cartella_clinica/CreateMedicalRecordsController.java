package controllers.cartella_clinica;

import db.ConnectionProvider;
import db.Table;
import db.tables.MedicalRecordsTables;
import db.tables.PatientsTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.MedicalRecords;
import model.PersonRelated;

import java.util.List;
import java.util.function.Function;

import static utilities.checkers.PersonCheckers.*;


public class CreateMedicalRecordsController {
    @FXML
    public TextField idField, fiscalCodeField;
    @FXML
    public TextArea anamnesisArea, diagnosisArea, rehabArea;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final MedicalRecordsTables mrTables = new MedicalRecordsTables(connectionProvider.getMySQLConnection());
    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());


    public void create() {
        if (
                intCheck(idField, 1, 5) &
                isNotAlreadyPresent() &
                lengthChecker(fiscalCodeField, 16, 16) &
                checkPatientExistence() &
                lengthChecker(anamnesisArea, 1, 255) &
                lengthChecker(diagnosisArea, 1, 255) &
                lengthChecker(rehabArea, 1, 255)
        ) {
            var id = Integer.parseInt(idField.getText());
            var fiscalCode = fiscalCodeField.getText();
            var anamnesis = anamnesisArea.getText();
            var diagnosis = diagnosisArea.getText();
            var rehabProject = rehabArea.getText();

            mrTables.save(new MedicalRecords(id, fiscalCode, anamnesis, diagnosis, rehabProject));
        }
    }

    private boolean checkPatientExistence() {
        final var retPatient = patientsTables.findByCode(toUpperNormalizer(fiscalCodeField));

        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input FiscalCode doesn't exist");

        if (retPatient.isEmpty()) {
            errorAlert.showAndWait();
            return false;
        } else if (retPatient.get().size() == 0) {
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean isNotAlreadyPresent() {
        var list = mrTables.findAll();
        List<?> idList = list.stream().map(MedicalRecords::medicalRecordId).toList();
        if (idList.contains(Integer.parseInt(idField.getText()))) {
            final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The input \"" + idField.getId() + "\" already exists");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }
}
