package controllers.dipendente;

import utilities.ConnectionProvider;
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

    private String id;
    private String name;
    private String surname;
    private String residence;
    private String gender;
    private String ed;
    private Date birth;
    private int workerId;
    private int ecm;
    private boolean suitability;
    private boolean partner;

    public void create() {
        if (check() & isNotAlreadyPresent(idField, workersTable, PersonRelated::fiscalCode)) {
            this.init();

            workersTable.save(new Worker(id, name, surname, birth, residence, gender,
                    workerId, ed, suitability, partner, ecm));
        }
    }

    public void update() {
        if (check()) {
            this.init();

            workersTable.update(new Worker(id, name, surname, birth, residence, gender,
                    workerId, ed, suitability, partner, ecm));
        }
    }
    private boolean check() {
        return lengthChecker(idField, 16, 16) &
                lengthChecker(nameField, 2, 15) &
                lengthChecker(surnameField, 2, 15) &
                birthAndCheck(birthPicker, List.of(suitabilityCheck)) &
                lengthChecker(residenceField, 10, 50) &
                genderCheck(genderField) &
                intCheck(workerIdField, 1, 10) &
                lengthChecker(edField, 10, 50) &
                intCheck(ecmField, 0, 5);
    }

    private void init() {
        id = toUpperNormalizer(idField);
        name = nameField.getText();
        surname = surnameField.getText();
        residence = residenceField.getText();
        gender = toUpperNormalizer(genderField);
        ed = edField.getText();
        birth = Date.from(Instant.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        workerId = Integer.parseInt(workerIdField.getText());
        ecm = Integer.parseInt(ecmField.getText());
        suitability = suitabilityCheck.isSelected();
        partner = partnerCheck.isSelected();
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
