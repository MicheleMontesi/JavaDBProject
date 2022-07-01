package controllers.terapia;

import db.ConnectionProvider;
import db.tables.TherapiesTable;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Therapy;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static utilities.checkers.CommonCheckers.dateCheck;
import static utilities.checkers.PersonCheckers.intCheck;

public class CreateTherapyController {
    @FXML
    public TextField therapyField;
    @FXML
    public DatePicker datePicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());

    public void create() {
        if(
            intCheck(therapyField, 1, 10) &
            dateCheck(datePicker)
        ) {
            var therapyId = Integer.parseInt(therapyField.getText());
            var date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));

            therapiesTable.save(new Therapy(therapyId, date));
        }
    }
}
