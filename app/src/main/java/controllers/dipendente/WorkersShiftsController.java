package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.ShiftsTables;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Shift;
import utilities.CreateShiftView;

import java.time.LocalTime;

import static utilities.checkers.PersonCheckers.lengthChecker;

public class WorkersShiftsController {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Shift> table;
    @FXML
    private TableColumn<Shift,String> dayColumn;
    @FXML
    private TableColumn<Shift, LocalTime> beginColumn, endColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ShiftsTables shiftsTable = new ShiftsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var shift = shiftsTable.findByCode(idField.getText());
            if (shift.isPresent()) {
                final ObservableList<Shift> list = FXCollections.observableArrayList(shift.get());
                CreateShiftView.create(table, dayColumn, beginColumn, endColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idField.getText() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    public void disableOnWrite() {
        BooleanBinding idFieldValid = Bindings.createBooleanBinding(() -> idField.getText().isEmpty(), idField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
