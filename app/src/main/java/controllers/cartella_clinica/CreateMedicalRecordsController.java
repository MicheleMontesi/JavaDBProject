package controllers.cartella_clinica;

import db.ConnectionProvider;
import db.tables.MedicalRecordsTables;
import db.tables.PatientsTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.MedicalRecords;

import java.util.List;

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

    private int id;
    private String fiscalCode;
    private String anamnesis;
    private String diagnosis;
    private String rehabProject;

    public void create() {
        if (check() & isNotAlreadyPresent()) {
            this.init();
            mrTables.save(new MedicalRecords(id, fiscalCode, anamnesis, diagnosis, rehabProject));
        }
    }

    public void update() {
        if (check()) {
            this.init();
            mrTables.update(new MedicalRecords(id, fiscalCode, anamnesis, diagnosis, rehabProject));
        }
    }

    private boolean check() {
        return intCheck(idField, 1, 5) &
                lengthChecker(fiscalCodeField, 16, 16) &
                checkPatientExistence() &
                lengthChecker(anamnesisArea, 1, 255) &
                lengthChecker(diagnosisArea, 1, 255) &
                lengthChecker(rehabArea, 1, 255);
    }

    private void init() {
        id = Integer.parseInt(idField.getText());
        fiscalCode = fiscalCodeField.getText();
        anamnesis = anamnesisArea.getText();
        diagnosis = diagnosisArea.getText();
        rehabProject = rehabArea.getText();
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
