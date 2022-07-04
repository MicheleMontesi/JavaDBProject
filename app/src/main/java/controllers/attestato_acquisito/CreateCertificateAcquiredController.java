package controllers.attestato_acquisito;

import db.ConnectionProvider;
import db.Table;
import db.tables.CertificateAcquiredTables;
import db.tables.CertificateTypeTables;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.CertificateAcquired;
import utilities.checkers.CommonCheckers;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class CreateCertificateAcquiredController {

    @FXML
    private TextField idField, nameField;
    @FXML
    private DatePicker acquisitionPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final CertificateAcquiredTables caTables = new CertificateAcquiredTables(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());
    private final CertificateTypeTables ctTable = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                lengthChecker(idField, 16, 16) &
                caCheck(caTables, idField, nameField, acquisitionPicker) &
                lengthChecker(nameField, 2, 30) &
                checkOpUnitExistence()
        ) {
            var id = toUpperNormalizer(idField);
            var date = Date.from(Instant.from(acquisitionPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var name = toUpperNormalizer(nameField);

            caTables.save(new CertificateAcquired(id, date, name));
        }
    }

    private boolean caCheck(Table<CertificateAcquired, String> table, TextField idField, TextField nameField, DatePicker acquisitionPicker) {
        var list = table.findAll();
        var date = Date.from(Instant.from(acquisitionPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        for (var ca : list) {
            if (ca.fiscalCode().equals(toUpperNormalizer(idField)) &&
                    ca.certificateName().equals(toUpperNormalizer(nameField))) {
                if (getYearDifference(date, ca) < 3) {
                    errorAlert.setContentText("The input date must be 3 years after the last certificate with the same name");
                    errorAlert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkOpUnitExistence() {
        final var idCheck = workersTables.findByCode(toUpperNormalizer(idField));
        final var nameCheck = ctTable.findByCode(toUpperNormalizer(nameField));
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input id or name doesn't exist");
        return CommonCheckers.fieldChecker(List.of(idCheck, nameCheck));
    }

    private int getYearDifference(Date date, CertificateAcquired ca) {
        Calendar newDate = getCalendar(date);
        Calendar oldDate = getCalendar(ca.acquisitionDate());
        return CommonCheckers.getYears(newDate, oldDate);
    }

    private Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.setTime(date);
        return cal;
    }
}
