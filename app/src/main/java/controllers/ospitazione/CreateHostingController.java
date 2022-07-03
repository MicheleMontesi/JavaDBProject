package controllers.ospitazione;

import db.ConnectionProvider;
import db.tables.HostingTables;
import db.tables.OperatingUnitTables;
import db.tables.PatientsTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Hosting;
import utilities.checkers.CommonCheckers;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;


public class CreateHostingController {

    @FXML
    public TextField idField, unitIdField;
    @FXML
    public DatePicker beginPicker, endPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final HostingTables hostingTables = new HostingTables(connectionProvider.getMySQLConnection());
    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());
    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                lengthChecker(idField, 16, 16) &
                CommonCheckers.dateCheck(beginPicker) &
                lengthChecker(unitIdField, 1, 5) &
                checkFieldsExistence()
        ) {
            var id = toUpperNormalizer(idField);
            var begin = Date.from(Instant.from(beginPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var unitId = toUpperNormalizer(unitIdField);
            Optional<Date> endDate = Optional.empty();
            dateChecker(id, begin, unitId, endDate);
        }
    }

    private boolean checkFieldsExistence() {
        final var retUnit = operatingUnitTables.findByCode(toUpperNormalizer(unitIdField));
        final var retPatient = patientsTables.findByCode(toUpperNormalizer(idField));
        return CommonCheckers.fieldChecker(List.of(retUnit, retPatient));
    }

    private void dateChecker(String id, Date begin, String unitId, Optional<Date> endDate) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        if (beginPicker.getValue() != null) {
            final var list = hostingTables.findAll();

            for (var host : list) {
                if (host.fiscalCode().equals(toUpperNormalizer(idField))) {
                    if (host.endDate().isPresent()) {
                        if (CommonCheckers.getYearDifference(host.endDate().get(), begin) <= 0) {
                            errorAlert.setContentText("The input begin date must be one year bigger than the previous end date");
                            errorAlert.showAndWait();
                            return;
                        }
                    } else {
                        errorAlert.setContentText("The previous hosting must be ended before");
                        errorAlert.showAndWait();
                        return;
                    }
                }
            }

            if (endPicker.getValue() != null) {
                endDate = Optional.of(Date.from(Instant.from(endPicker.getValue().atStartOfDay(ZoneId.systemDefault()))));
                if (CommonCheckers.getYearDifference(begin, endDate.get()) < 0) {
                    errorAlert.setContentText("The input end date must be one year bigger than the begin date");
                    errorAlert.showAndWait();
                    return;
                }
                hostingTables.save(new Hosting(id, begin, endDate, unitId));
            } else {
                hostingTables.save(new Hosting(id, begin, endDate, unitId));
            }
        }
    }
}
