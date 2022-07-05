package controllers.turno;

import db.ConnectionProvider;
import db.tables.ShiftsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Shift;
import utilities.views.CreateShiftView;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ShiftsViewController implements Initializable {
    @FXML
    private TableView<Shift> table;
    @FXML
    private TableColumn<Shift, String> idColumn, dayColumn, unitColumn;
    @FXML
    private TableColumn<Shift, LocalTime> beginColumn, endColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ShiftsTables shiftsTables = new ShiftsTables(connectionProvider.getMySQLConnection());

    private final ObservableList<Shift> list = FXCollections.observableArrayList(shiftsTables.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateShiftView.create(table, idColumn, dayColumn, beginColumn, endColumn, unitColumn, list);

    }
}
