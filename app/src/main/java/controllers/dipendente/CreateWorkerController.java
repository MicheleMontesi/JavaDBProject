package controllers.dipendente;

import javafx.scene.control.ChoiceBox;
import model.Entity;
import utilities.ConnectionProvider;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Worker;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import static utilities.FillUtils.getList;
import static utilities.DateConverter.dateToLocalDate;
import static utilities.checkers.PersonCheckers.*;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class CreateWorkerController implements Initializable {

    @FXML
    private TextField idField, nameField, surnameField, residenceField,
            workerIdField, edField, ecmField;
    @FXML
    private CheckBox suitabilityCheck, partnerCheck;
    @FXML
    private DatePicker birthPicker;
    @FXML
    private ChoiceBox<String> genderBox, idBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final WorkersTables workersTable = new WorkersTables(connectionProvider.getMySQLConnection());

    private String id, name, surname, residence, gender, ed;
    private Date birth;
    private int workerId;
    private int ecm;
    private boolean suitability;
    private boolean partner;

    public void create() {
        if (
                check() &&
                isNotAlreadyPresent(idField, workersTable, Entity::getId) &&
                lengthChecker(idField, 16, 16)
        ) {
            this.init();
            id = toUpperNormalizer(idField);
            workersTable.save(new Worker(id, name, surname, birth, residence, gender,
                    workerId, ed, suitability, partner, ecm));
        }
    }

    public void update() {
        if (check() && choiceBoxChecker(idBox)) {
            this.init();
            id = idBox.getValue();
            workersTable.update(new Worker(id, name, surname, birth, residence, gender,
                    workerId, ed, suitability, partner, ecm));
        }
    }
    private boolean check() {
        return lengthChecker(nameField, 2, 15) &
                lengthChecker(surnameField, 2, 15) &
                birthAndCheck(birthPicker, List.of(suitabilityCheck)) &
                lengthChecker(residenceField, 10, 50) &
                intCheck(workerIdField, 1, 10) &
                lengthChecker(edField, 10, 50) &
                intCheck(ecmField, 0, 5);
    }

    private void init() {
        name = nameField.getText();
        surname = surnameField.getText();
        residence = residenceField.getText();
        gender = genderBox.getValue();
        ed = edField.getText();
        birth = Date.from(Instant.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        workerId = Integer.parseInt(workerIdField.getText());
        ecm = Integer.parseInt(ecmField.getText());
        suitability = suitabilityCheck.isSelected();
        partner = partnerCheck.isSelected();
    }

    public void fillFields() {
        if (!idBox.getSelectionModel().isEmpty()) {
            var selected = idBox.getSelectionModel().getSelectedItem();
            var workersList = workersTable.findByCode(selected);
            if (workersList.isPresent()) {
                var worker = workersList.get().stream().findFirst().orElse(null);
                if (worker != null) {
                    nameField.setText(worker.name());
                    surnameField.setText(worker.surname());
                    birthPicker.setValue(dateToLocalDate(worker.birthDay().toString()));
                    residenceField.setText(worker.residence());
                    genderBox.setValue(worker.gender());
                    workerIdField.setText(String.valueOf(worker.workerId()));
                    edField.setText(worker.edQualification());
                    ecmField.setText(Objects.toString(worker.ECMCredits()));
                    suitabilityCheck.setSelected(true);
                    partnerCheck.setSelected(worker.partner());
                }
            }
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

        if (genderBox != null) {
            genderBox.getItems().addAll(new ArrayList<>(Arrays.asList("M", "F")));
        }
        getList(idBox, workersTable, e -> e.getId().get(0));
    }
}
