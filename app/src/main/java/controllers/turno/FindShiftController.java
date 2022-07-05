package controllers.turno;

import db.ConnectionProvider;
import db.tables.ShiftsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Shift;
import utilities.views.CreateShiftView;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static utilities.FXUtils.disableOnWrite;
import static utilities.checkers.PersonCheckers.lengthChecker;

public class FindShiftController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Shift> table;
    @FXML
    private TableColumn<Shift, String> idColumn, dayColumn, unitColumn;
    @FXML
    private TableColumn<Shift, LocalTime> beginColumn, endColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ShiftsTables shiftsTables = new ShiftsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var shift = shiftsTables.findByCode(idField.getText());
            if (shift.isPresent()) {
                final ObservableList<Shift> list = FXCollections.observableArrayList(shift.get());
                CreateShiftView.create(table, idColumn, dayColumn, beginColumn, endColumn, unitColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idField.getText() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableOnWrite(searchButton, idField);
    }
}
