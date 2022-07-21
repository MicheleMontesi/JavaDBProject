package controllers.turno;

import utilities.ConnectionProvider;
import db.Table;
import db.tables.OperatingUnitTables;
import db.tables.ShiftsTables;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.OperatingUnit;
import model.Shift;
import model.Worker;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.timeCheck;

public class CreateShiftController implements Initializable {

    @FXML
    private TextField beginField, endField;
    @FXML
    private ChoiceBox<String> idBox, dayBox, ouBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ShiftsTables shiftsTable = new ShiftsTables(connectionProvider.getMySQLConnection());
    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());

    private String id;
    private String day;
    private LocalTime begin;
    private LocalTime end;
    private String unitId;

    public void create() {
        if (check()) {
            this.init();
            shiftsTable.save(new Shift(id, day, begin, end, unitId));
        }
    }

    public void update() {
        if (check()) {
            this.init();
            shiftsTable.update(new Shift(id, day, begin, end, unitId));
        }
    }

    private boolean check() {
        return lengthChecker(beginField, 5, 5) &
                lengthChecker(endField, 5, 5) &
                timeCheck(beginField) &
                timeCheck(endField) &
                shiftCheck(shiftsTable, idBox, dayBox, beginField, endField); // bisogna che questa chiave esista giá
    }

    private void init() {
        id = idBox.getValue();
        day = dayBox.getValue();
        begin = LocalTime.parse(beginField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        end = LocalTime.parse(endField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        unitId = ouBox.getValue();
    }

    private boolean shiftCheck(Table<Shift, String> table,
                               ChoiceBox<String> idBox, ChoiceBox<String> dayBox, TextField beginField, TextField endField) {
        var list = table.findAll();
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        final var inputEndTime = LocalTime.parse(endField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        final var inputStartTime = LocalTime.parse(beginField.getText(), DateTimeFormatter.ofPattern("HH:mm"));

        for (var shift : list) {
            if (shift.unitId().equalsIgnoreCase(idBox.getValue()) &&
            shift.dayOfTheWeek().equalsIgnoreCase(dayBox.getValue())) {
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
                    errorAlert.setContentText("The input \"" + idBox.getValue() + "\" " + "\"" + dayBox.getValue() +
                            "\" " + "\"" + beginField.getText() + "\" already exists");
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(workersTables.findAll().stream().map(Worker::fiscalCode).toList());
        }
        if (dayBox != null) {
            dayBox.getItems().addAll(new ArrayList<>(Arrays.asList("LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI",
                    "VENERDI", "SABATO", "DOMENICA")));
        }
        if (ouBox != null) {
            ouBox.getItems().addAll(operatingUnitTables.findAll().stream().map(OperatingUnit::unitId).toList());
        }
    }
}
