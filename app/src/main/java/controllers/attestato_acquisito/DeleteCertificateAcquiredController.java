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
import utilities.ConnectionProvider;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.*;

public class DeleteCertificateAcquiredController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox, nameBox;
    @FXML
    private DatePicker datePicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final CertificateAcquiredTables caTable = new CertificateAcquiredTables(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());
    private final CertificateTypeTables ctTable = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                choiceBoxChecker(idBox) &&
                dateCheck(datePicker) &&
                choiceBoxChecker(nameBox) &&
                checkExistence(Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()))))
        ) {
            final var id = idBox.getValue();
            final var date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final var name = nameBox.getValue();
            var worker = CreateCertificateAcquiredController.findWorker(id);
            var cert = CreateCertificateAcquiredController.findCT(name);
            CreateCertificateAcquiredController.sumECM(worker, cert, true);

            caTable.deleteByParameters(id, date, name);
        }
    }

    private boolean checkExistence(Date date) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input doesn't exist");
        for (var ca : caTable.findAll()) {
            if (!ca.fiscalCode().equals(idBox.getValue())
                    & !ca.acquisitionDate().equals(date)
                    & !ca.certificateName().equals(nameBox.getValue())) {
                    errorAlert.showAndWait();
                    return false;
            }
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(caTable.findAll().stream().map(CertificateAcquired::fiscalCode).distinct().toList());
        }
        if (nameBox != null) {
            nameBox.getItems().addAll(ctTable.findAll().stream().map(CertificateType::name).toList());
        }
    }
}
