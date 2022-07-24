package controllers.turno;

import db.tables.ShiftsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;

import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.FillUtils.*;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteShiftController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox, dayBox, beginBox, ouBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final ShiftsTables shiftsTables = new ShiftsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                choiceBoxChecker(idBox) &&
                choiceBoxChecker(dayBox) &&
                choiceBoxChecker(beginBox) &&
                choiceBoxChecker(ouBox)
        ) {
            final String id = idBox.getValue();
            final Time begin = Time.valueOf(LocalTime.parse(beginBox.getValue(), DateTimeFormatter.ofPattern("HH:mm")));
            final String day = dayBox.getValue();
            final String ou = ouBox.getValue();

            shiftsTables.deleteByParameters(id, begin, day, ou);
        }
    }

    public void fillDayField() {
        fillRelatedField(idBox, dayBox, shiftsTables, 0, 1);
    }
    public void fillTimeField() {
        if (!idBox.getSelectionModel().isEmpty()) {
            if (!dayBox.getSelectionModel().isEmpty()) {
                if (beginBox != null) {
                    beginBox.getItems().removeAll(beginBox.getItems());
                    beginBox.getItems().addAll(shiftsTables.findAll().stream()
                            .filter(e -> e.getId().get(0).equals(idBox.getValue()) &&
                                    e.getId().get(1).equals(dayBox.getValue()))
                            .map(e -> e.getId().get(2)).map(Objects::toString).toList());
                }
            }
        }
    }
    public void fillOUField() {
        fillRelatedField(idBox, ouBox, shiftsTables, 0, 3);
        if (!idBox.getSelectionModel().isEmpty()) {
            if (!dayBox.getSelectionModel().isEmpty()) {
                if (!ouBox.getSelectionModel().isEmpty()) {
                    if (ouBox != null) {
                        ouBox.getItems().removeAll(ouBox.getItems());
                        ouBox.getItems().addAll(shiftsTables.findAll().stream()
                                .filter(e -> e.getId().get(0).equals(idBox.getValue()) &&
                                        e.getId().get(1).equals(dayBox.getValue()) &&
                                        e.getId().get(2).equals(ouBox.getValue()))
                                .map(e -> e.getId().get(3)).map(Objects::toString).distinct().toList());
                    }
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, shiftsTables, e -> e.getId().get(0));
    }
}
