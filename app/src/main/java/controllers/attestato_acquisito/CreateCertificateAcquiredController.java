package controllers.attestato_acquisito;

import utilities.ConnectionProvider;
import db.Table;
import db.tables.CertificateAcquiredTables;
import db.tables.CertificateTypeTables;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.CertificateAcquired;
import model.Worker;
import utilities.checkers.CommonCheckers;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class CreateCertificateAcquiredController {

    @FXML
    private TextField idField, nameField;
    @FXML
    private DatePicker acquisitionPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final CertificateAcquiredTables caTables = new CertificateAcquiredTables(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());
    private final CertificateTypeTables ctTable = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (check()) {
            var id = toUpperNormalizer(idField);
            var date = Date.from(Instant.from(acquisitionPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var name = toUpperNormalizer(nameField);
            var worker = workersTables.findByCode(id).isPresent() ?
                    workersTables.findByCode(id).get().stream().findFirst().isPresent() ?
                            workersTables.findByCode(id).get().stream().findFirst().get() : null
                    : null;
            var cert = ctTable.findByCode(name).isPresent() ?
                    ctTable.findByCode(name).get().stream().findFirst().isPresent() ?
                            ctTable.findByCode(name).get().stream().findFirst().get() : null
                    : null;
            if (worker != null && cert != null) {
                var credits = worker.ECMCredits() + cert.ecmCredits();
                var newWorker = new Worker(worker.fiscalCode(), worker.name(), worker.surname(), worker.birthDay(),
                        worker.residence(), worker.gender(), worker.workerId(), worker.edQualification(),
                        worker.suitability(), worker.partner(), credits);
                workersTables.update(newWorker);
            }
            caTables.save(new CertificateAcquired(id, date, name));
        }
    }

    public void update() {
        if (check()) {
            var id = toUpperNormalizer(idField);
            var date = Date.from(Instant.from(acquisitionPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var name = toUpperNormalizer(nameField);

            caTables.update(new CertificateAcquired(id, date, name));
        }
    }

    private boolean check() {
        return lengthChecker(idField, 16, 16) &
                caCheck(caTables, idField, nameField, acquisitionPicker) &
                lengthChecker(nameField, 2, 30) &
                CommonCheckers.checkOpUnitExistence(workersTables, idField, ctTable, nameField);
    }

    private boolean caCheck(Table<CertificateAcquired, String> table, TextField idField, TextField nameField, DatePicker acquisitionPicker) {
        var list = table.findAll();
        var date = Date.from(Instant.from(acquisitionPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        for (var ca : list) {
            if (ca.fiscalCode().equals(toUpperNormalizer(idField)) &&
                    ca.certificateName().equals(toUpperNormalizer(nameField))) {
                if (CommonCheckers.getYearDifference(date, ca.acquisitionDate()) < 3) {
                    errorAlert.setContentText("The input date must be 3 years after the last certificate with the same name");
                    errorAlert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }
}
