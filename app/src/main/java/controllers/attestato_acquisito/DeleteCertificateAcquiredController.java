package controllers.attestato_acquisito;

import utilities.ConnectionProvider;
import db.tables.CertificateAcquiredTables;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static utilities.checkers.PersonCheckers.*;
import static utilities.checkers.CommonCheckers.*;

public class DeleteCertificateAcquiredController {

    @FXML
    private TextField idField, nameField;
    @FXML
    private DatePicker datePicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final CertificateAcquiredTables caTable = new CertificateAcquiredTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                lengthChecker(idField, 16, 16) &
                dateCheck(datePicker) &
                lengthChecker(nameField, 2, 30)
        ) {
            final var id = toUpperNormalizer(idField);
            final var date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final var name = nameField.getText();

            caTable.deleteByParameters(id, date, name);
        }
    }
}
