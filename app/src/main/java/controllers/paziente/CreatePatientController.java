package controllers.paziente;

import db.ConnectionProvider;
import db.tables.PatientsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Patient;
import model.PersonRelated;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.*;

public class CreatePatientController implements Initializable {
    
    @FXML
    public TextField idField, nameField, surnameField, residenceField, genderField, patientIdField;
    @FXML
    public DatePicker birthPicker;
    @FXML
    public CheckBox privacyCheck, consentCheck, acceptanceCheck;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                lengthChecker(idField, 16, 16) &
                isNotAlreadyPresent(idField, patientsTables, PersonRelated::fiscalCode) &
                lengthChecker(nameField, 2, 15) &
                lengthChecker(surnameField, 2, 15) &
                birthAndCheck(birthPicker, List.of(privacyCheck, consentCheck, acceptanceCheck)) &
                lengthChecker(residenceField, 10, 50) &
                genderCheck(genderField)
        ) {
            final String id = toUpperNormalizer(idField);
            final String name = nameField.getText();
            final String surname = surnameField.getText();
            final String residence = residenceField.getText();
            final String gender = genderField.getText().toUpperCase();
            final Date birth = Date.from(Instant.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final int patientId = Integer.parseInt(patientIdField.getText());
            final boolean privacy = privacyCheck.isSelected();
            final boolean consent = consentCheck.isSelected();
            final boolean acceptance = acceptanceCheck.isSelected();

            patientsTables.save(new Patient(id, name, surname, birth, residence, gender,
                    patientId, privacy, consent, acceptance));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var max = patientsTables.findAll()
                .stream()
                .map(Patient::patientId)
                .toList()
                .stream()
                .mapToInt(v->v)
                .max()
                .orElse(0);
        patientIdField.setText(Integer.toString(max + 1));
    }
}
