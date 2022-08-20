package controllers.paziente;

import model.Entity;
import utilities.ConnectionProvider;
import db.tables.PatientsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Patient;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import static utilities.checkers.PersonCheckers.*;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;
import static utilities.FillUtils.*;
import static utilities.DateConverter.dateToLocalDate;

public class CreatePatientController implements Initializable {
    
    @FXML
    private TextField idField, nameField, surnameField, residenceField, patientIdField;
    @FXML
    private DatePicker birthPicker;
    @FXML
    private CheckBox privacyCheck, consentCheck, acceptanceCheck;
    @FXML
    private ChoiceBox<String> fiscalCodeBox, genderBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    private String id, name, surname, residence, gender;
    private Date birth;
    private int patientId;
    private boolean privacy, consent, acceptance;

    public void create() {
        if (
                check() &
                lengthChecker(idField, 16, 16) &
                isNotAlreadyPresent(idField, patientsTables, Entity::getId)
        ) {
            this.init();
            id = toUpperNormalizer(idField);
            patientsTables.save(new Patient(id, name, surname, birth, residence, gender,
                    patientId, privacy, consent, acceptance));
        }
    }

    public void update() {
        if (check() && choiceBoxChecker(fiscalCodeBox)) {
            this.init();
            id = fiscalCodeBox.getValue();
            patientsTables.update(new Patient(id, name, surname, birth, residence, gender,
                    patientId, privacy, consent, acceptance));
        }
    }

    private boolean check() {
        return lengthChecker(nameField, 2, 15) &
                lengthChecker(surnameField, 2, 15) &
                birthAndCheck(birthPicker, List.of(privacyCheck, consentCheck, acceptanceCheck)) &
                lengthChecker(residenceField, 2, 100);
    }

    private void init() {
        name = nameField.getText();
        surname = surnameField.getText();
        residence = residenceField.getText();
        gender = genderBox.getValue();
        birth = Date.from(Instant.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        patientId = Integer.parseInt(patientIdField.getText());
        privacy = privacyCheck.isSelected();
        consent = consentCheck.isSelected();
        acceptance = acceptanceCheck.isSelected();
    }

    public void fillFields() {
        if (!fiscalCodeBox.getSelectionModel().isEmpty()) {
            var selected = fiscalCodeBox.getSelectionModel().getSelectedItem();
            var patientList = patientsTables.findByCode(selected);
            if (patientList.isPresent()) {
                var patient = patientList.get().stream().findFirst().orElse(null);
                if (patient != null) {
                    nameField.setText(patient.name());
                    surnameField.setText(patient.surname());
                    residenceField.setText(patient.residence());
                    genderBox.setValue(patient.gender());
                    birthPicker.setValue(dateToLocalDate(patient.birthday().toString()));
                    patientIdField.setText(String.valueOf(patient.patientId()));
                    privacyCheck.setSelected(true);
                    consentCheck.setSelected(true);
                    acceptanceCheck.setSelected(true);
                }
            }
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

        if (genderBox != null) {
            genderBox.getItems().addAll(new ArrayList<>(Arrays.asList("M", "F")));
            genderBox.setValue("M");
        }

        getList(fiscalCodeBox, patientsTables, e -> e.getId().get(0));
    }
}
