package controllers.terapia;

import utilities.ConnectionProvider;
import db.tables.TherapiesTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Therapy;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;
import static utilities.checkers.CommonCheckers.dateCheck;
import static utilities.checkers.PersonCheckers.intCheck;
import static utilities.FillUtils.getList;
import static utilities.DateConverter.dateToLocalDate;

public class CreateTherapyController implements Initializable {
    @FXML
    public TextField therapyField;
    @FXML
    public DatePicker datePicker;
    @FXML
    private ChoiceBox<String> idBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());

    private int therapyId;
    private Date date;

    public void create() {
        if (
                intCheck(therapyField, 1, 10) &
                dateCheck(datePicker)
        ) {
            therapyId = Integer.parseInt(therapyField.getText());
            date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            therapiesTable.save(new Therapy(therapyId, date));
        }
    }

    public void update() {
        if (
                choiceBoxChecker(idBox) &
                dateCheck(datePicker)
        ) {
            therapyId = Integer.parseInt(idBox.getValue());
            date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            therapiesTable.update(new Therapy(therapyId, date));
        }
    }

    public void fillFields() {
        if (!idBox.getSelectionModel().isEmpty()) {
            var selected = idBox.getSelectionModel().getSelectedItem();
            var therapyList = therapiesTable.findByCode(selected);
            if (therapyList.isPresent()) {
                var therapy = therapyList.get().stream().findFirst().isPresent() ?
                        therapyList.get().stream().findFirst().get() : null;
                if (therapy != null) {
                    datePicker.setValue(dateToLocalDate(therapy.creationDate().toString()));
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, therapiesTable, e -> e.getId().get(0));
    }
}
