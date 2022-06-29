package controllers.turno;

import db.ConnectionProvider;
import db.tables.ShiftsTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.PersonRelated;
import model.Shift;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static utilities.checkers.PersonCheckers.*;

public class CreateShiftController {

    @FXML
    private TextField idField, dayField, beginField, endField, unitIdField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ShiftsTables shiftsTable = new ShiftsTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
            lengthChecker(idField, 16, 16) &
            isNotAlreadyPresent(idField, shiftsTable, PersonRelated::fiscalCode) &
            dayCheck(dayField) &
            timeCheck(beginField) &
            timeCheck(endField) &
            lengthChecker(unitIdField, 1, 5) // bisogna fare una lista che prenda le chiavi da OperatingUnit
        ) {
            final String id = idField.getText();
            final String day = dayField.getText();
            final LocalTime begin = LocalTime.parse(beginField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            final LocalTime end = LocalTime.parse(endField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            final String unitId = unitIdField.getText();
            shiftsTable.save(new Shift(id, day, begin, end, unitId));

        }
    }
}
