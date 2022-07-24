package controllers.cartella_clinica;

import db.tables.MedicalRecordsTables;
import db.tables.PatientsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.MedicalRecords;
import model.Patient;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;
import static utilities.checkers.PersonCheckers.intCheck;
import static utilities.checkers.PersonCheckers.lengthChecker;


public class CreateMedicalRecordsController implements Initializable {
    @FXML
    public TextField idField;
    @FXML
    public ChoiceBox<String> fiscalCodeBox, idBox;
    @FXML
    public TextArea anamnesisArea, diagnosisArea, rehabArea;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final MedicalRecordsTables mrTables = new MedicalRecordsTables(connectionProvider.getMySQLConnection());
    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    private int id;
    private String fiscalCode;
    private String anamnesis;
    private String diagnosis;
    private String rehabProject;

    public void create() {
        if (check() && isNotAlreadyPresent() && intCheck(idField, 1, 5)) {
            this.init();
            id = Integer.parseInt(idField.getText());
            mrTables.save(new MedicalRecords(id, fiscalCode, anamnesis, diagnosis, rehabProject));
        }
    }

    public void update() {
        if (check() && choiceBoxChecker(idBox)) {
            this.init();
            id = Integer.parseInt(idBox.getValue());
            mrTables.update(new MedicalRecords(id, fiscalCode, anamnesis, diagnosis, rehabProject));
        }
    }

    private boolean check() {
        return  choiceBoxChecker(fiscalCodeBox) &
                checkPatientExistence() &
                lengthChecker(anamnesisArea, 1, 255) &
                lengthChecker(diagnosisArea, 1, 255) &
                lengthChecker(rehabArea, 1, 255);
    }

    private void init() {
        fiscalCode = fiscalCodeBox.getValue();
        anamnesis = anamnesisArea.getText();
        diagnosis = diagnosisArea.getText();
        rehabProject = rehabArea.getText();
    }

    private boolean checkPatientExistence() {
        final var retPatient = patientsTables.findByCode(fiscalCodeBox.getValue());

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

    public void fillFields() {
        if (!idBox.getSelectionModel().isEmpty()) {
            var selectedMR = idBox.getSelectionModel().getSelectedItem();
            var mrList = mrTables.findByCode(selectedMR);
            if (mrList.isPresent()) {
                var mr = mrList.get().stream().findFirst().orElse(null);
                if (mr != null) {
                    fiscalCodeBox.setValue(mr.fiscalCode());
                    anamnesisArea.setText(mr.anamnesis());
                    diagnosisArea.setText(mr.diagnosis());
                    rehabArea.setText(mr.rehabProject());
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(fiscalCodeBox, patientsTables, e -> e.getId().get(0));
        getList(idBox, mrTables, e -> e.getId().get(0));
    }
}
