package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.PersonRelated;
import model.Worker;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.*;

public class CreateWorkerController implements Initializable {

    @FXML
    private TextField idField, nameField, surnameField, residenceField,
            genderField, workerIdField, edField, ecmField;
    @FXML
    private CheckBox suitabilityCheck, partnerCheck;
    @FXML
    private DatePicker birthPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final WorkersTables workersTable = new WorkersTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
            lengthChecker(idField, 16, 16) &
            isNotAlreadyPresent(idField, workersTable, PersonRelated::fiscalCode) &
            lengthChecker(nameField, 2, 15) &
            lengthChecker(surnameField, 2, 15) &
            birthAndCheck(birthPicker, List.of(suitabilityCheck)) &
            lengthChecker(residenceField, 10, 50) &
            genderCheck(genderField) &
            intCheck(workerIdField, 1, 10) &
            lengthChecker(edField, 10, 50) &
            intCheck(ecmField, 0, 5)
        ) {
            final String id = idField.getText();
            final String name = nameField.getText();
            final String surname = surnameField.getText();
            final String residence = residenceField.getText();
            final String gender = genderField.getText().toUpperCase();
            final String ed = edField.getText();
            final Date birth = Date.from(Instant.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final int workerId = Integer.parseInt(workerIdField.getText());
            final int ecm = Integer.parseInt(ecmField.getText());
            final boolean suitability = suitabilityCheck.isSelected();
            final boolean partner = partnerCheck.isSelected();

            workersTable.save(new Worker(id, name, surname, birth, residence, gender,
                    workerId, ed, suitability, partner, ecm));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var max = workersTable.findAll()
                .stream()
                .map(Worker::workerId)
                .toList()
                .stream()
                .mapToInt(v->v)
                .max()
                .orElse(0);
        workerIdField.setText(Integer.toString(max + 1));
    }
}
