package controllers.turno;

import db.ConnectionProvider;
import db.Table;
import db.tables.OperatingUnitTables;
import db.tables.ShiftsTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Shift;
import utilities.checkers.PersonCheckers;

import java.text.Normalizer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static utilities.checkers.PersonCheckers.*;

public class CreateShiftController {

    @FXML
    private TextField idField, dayField, beginField, endField, unitIdField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ShiftsTables shiftsTable = new ShiftsTables(connectionProvider.getMySQLConnection());
    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
            lengthChecker(idField, 16, 16) &
            dayCheck(dayField) &
            lengthChecker(beginField, 5, 5) &
            lengthChecker(endField, 5, 5) &
            timeCheck(beginField) &
            timeCheck(endField) &
            shiftCheck(shiftsTable, idField, dayField, beginField, endField) &
            lengthChecker(unitIdField, 1, 5) & // bisogna che questa chiave esista giá
            checkOpUnitExistence()
        ) {
            final String id = toUpperNormalizer(idField);
            final String day = toUpperNormalizer(dayField);
            final LocalTime begin = LocalTime.parse(beginField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            final LocalTime end = LocalTime.parse(endField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            final String unitId = toUpperNormalizer(unitIdField);
            shiftsTable.save(new Shift(id, day, begin, end, unitId));

        }
    }

    private boolean shiftCheck(Table<Shift, String> table,
                               TextField idField, TextField dayField, TextField beginField, TextField endField) {
        var list = table.findAll();
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        final var inputEndTime = LocalTime.parse(endField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        final var inputStartTime = LocalTime.parse(beginField.getText(), DateTimeFormatter.ofPattern("HH:mm"));

        for (var shift : list) {
            if (shift.unitId().equalsIgnoreCase(toUpperNormalizer(unitIdField)) &&
            shift.dayOfTheWeek().equalsIgnoreCase(toUpperNormalizer(dayField))) {
                if (inputStartTime.getHour() >= shift.beginTime().getHour() &&
                        inputStartTime.getHour() <= shift.endTime().getHour()) {
                    errorAlert.setContentText("The input begin time begins during an already existing shift");
                    errorAlert.showAndWait();
                    return false;
                }
                if (inputEndTime.getHour() >= shift.beginTime().getHour() &&
                       inputEndTime.getHour() <= shift.endTime().getHour()) {
                    errorAlert.setContentText("The input end time ends during an already existing shift");
                    errorAlert.showAndWait();
                    return false;
                }
                if (inputStartTime.getHour() <= shift.beginTime().getHour() &&
                        inputEndTime.getHour() >= shift.endTime().getHour()) {
                    errorAlert.setContentText("The input begins before an already existing shift and ends after that");
                    errorAlert.showAndWait();
                    return false;
                }
                if (shift.beginTime().compareTo(inputStartTime) == 0) {
                    errorAlert.setContentText("The input \"" + idField.getId() + "\" " + "\"" + dayField.getId() +
                            "\" " + "\"" + beginField.getId() + "\" already exists");
                    errorAlert.showAndWait();
                    return false;
                }
            }
        }
        if (inputEndTime.getHour() < inputStartTime.getHour() + 1) {
            errorAlert.setContentText("The input end is less than 1 hour from the begin");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean checkOpUnitExistence() {
        final var ret = operatingUnitTables.findByCode(toUpperNormalizer(unitIdField));
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input UnitId doesn't exist");
        if (ret.isEmpty()) {
            errorAlert.showAndWait();
            return false;
        } else {
            if (ret.get().size() == 0) {
                errorAlert.showAndWait();
                return false;
            }
        }
        return true;
    }
}
