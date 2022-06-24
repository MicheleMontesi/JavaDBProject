package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.WorkersTable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Worker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class CreateWorkerController {

    @FXML
    private TextField idField, nameField, surnameField, residenceField,
            genderField, workerCodeField, edField, ecmField;
    @FXML
    private CheckBox suitabilityCheck, partnerCheck;
    @FXML
    private DatePicker birthPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final WorkersTable workersTable = new WorkersTable(connectionProvider.getMySQLConnection());

    public void create() {
        if (
            lengthChecker(idField, 16, 16) &
            lengthChecker(nameField, 2, 25) &
            lengthChecker(surnameField, 2, 25) &
            lengthChecker(residenceField, 10, 50) &
            lengthChecker(genderField, 1, 1) &
            lengthChecker(edField, 10, 50) &
            lengthChecker(workerCodeField, 1, 10) &
            lengthChecker(ecmField, 0, 3) &
            birthAndSuitabilityCheck()
        ) {
            final String id = idField.getText();
            final String name = nameField.getText();
            final String surname = surnameField.getText();
            final String residence = residenceField.getText();
            final String gender = genderField.getText();
            final String ed = edField.getText();
            final Optional<Date> birth = birthPicker.getValue() != null ? Optional.of(Date.from(
                            Instant.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault())))) : Optional.empty();
            final int workerId = Integer.parseInt(workerCodeField.getText());
            final int ecm = Integer.parseInt(ecmField.getText());
            final boolean suitability = suitabilityCheck.isSelected();
            final boolean partner = partnerCheck.isSelected();

            workersTable.save(new Worker(id, name, surname, birth, residence, gender,
                    workerId, ed, suitability, partner, ecm));
        }

    }

    private boolean lengthChecker(TextField field, int minLength, int maxLength) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        if (field.getText().length() < minLength || field.getText().length() > maxLength) {
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The size of the field must be between " + minLength +
                    " and " + maxLength + " characters");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean birthAndSuitabilityCheck() {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        if (birthPicker.getValue() != null) {
            final Date birth = Date.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            var differenceInTime = new Date().getTime() - birth.getTime();
            var differenceInDays = differenceInTime / (1000*3600*24);
            var differenceInYears = differenceInDays /365;
            if (!((differenceInYears <= 80 && differenceInYears >= 18) && suitabilityCheck.isSelected())) {
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The data can be blank or must be between 80 years ago and 18 years ago\n" +
                        "and the field \"IdonitaAllaMansione\" must be checked");
                errorAlert.showAndWait();
                return false;
            }
            return true;
        }
        if (!suitabilityCheck.isSelected()) {
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The field \"IdonitaAllaMansione\" must be checked");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }
}
