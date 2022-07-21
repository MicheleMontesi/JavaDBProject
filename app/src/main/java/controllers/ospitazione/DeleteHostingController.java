package controllers.ospitazione;

import utilities.ConnectionProvider;
import db.tables.HostingTables;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static utilities.checkers.CommonCheckers.dateCheck;
import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class DeleteHostingController {

    @FXML
    private TextField fiscalCodeField, unitIdField;
    @FXML
    private DatePicker datePicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final HostingTables hostingTables = new HostingTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                lengthChecker(fiscalCodeField, 16, 16) &
                dateCheck(datePicker)
        ) {
            final var fiscalCode = toUpperNormalizer(fiscalCodeField);
            final var date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final var unitId = toUpperNormalizer(unitIdField);

            hostingTables.deleteByParameters(fiscalCode, date, unitId);
        }
    }
}
