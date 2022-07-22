package controllers.attestato_acquisito;

import db.tables.CertificateAcquiredTables;
import db.tables.CertificateTypeTables;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import model.CertificateAcquired;
import model.CertificateType;
import model.Worker;
import utilities.ConnectionProvider;
import utilities.checkers.CommonCheckers;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.checkExistence;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class CreateCertificateAcquiredController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox, nameBox;
    @FXML
    private DatePicker acquisitionPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final CertificateAcquiredTables caTables = new CertificateAcquiredTables(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());
    private final CertificateTypeTables ctTable = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (check()) {
            var id = idBox.getValue();
            var date = Date.from(Instant.from(acquisitionPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var name = nameBox.getValue();
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

    private boolean check() {
        return choiceBoxChecker(idBox) &
                choiceBoxChecker(nameBox) &
                caCheck() &
                checkExistence(workersTables, idBox.getValue(), ctTable, nameBox.getValue());
    }

    private boolean caCheck() {
        var list = caTables.findAll();
        var date = Date.from(Instant.from(acquisitionPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        for (var ca : list) {
            if (ca.fiscalCode().equals(idBox.getValue()) &&
                    ca.certificateName().equals(nameBox.getValue())) {
                if (CommonCheckers.getYearDifference(date, ca.acquisitionDate()) < 3) {
                    errorAlert.setContentText("The input date must be 3 years after the last certificate with the same name");
                    errorAlert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(workersTables.findAll().stream().map(Worker::fiscalCode).toList());
        }
        if (nameBox != null) {
            nameBox.getItems().addAll(ctTable.findAll().stream().map(CertificateType::name).toList());
        }
    }
}
