package controllers.turno;

import utilities.ConnectionProvider;
import db.tables.ShiftsTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.*;

public class DeleteShiftController {

    @FXML
    private TextField idField, dayField, timeField, unitIdField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ShiftsTables shiftsTables = new ShiftsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                lengthChecker(idField, 16, 16) &
                dayCheck(dayField) &
                timeCheck(timeField) &
                lengthChecker(unitIdField, 1, 5)
        ) {
            final String id = idField.getText();

            shiftsTables.delete(id);
        }
    }
}
