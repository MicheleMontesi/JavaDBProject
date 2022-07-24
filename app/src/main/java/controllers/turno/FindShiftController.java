package controllers.turno;

import db.tables.ShiftsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Shift;
import utilities.ConnectionProvider;
import utilities.views.CreateShiftView;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class FindShiftController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;
    @FXML
    private TableView<Shift> table;
    @FXML
    private TableColumn<Shift, String> idColumn, dayColumn, unitColumn;
    @FXML
    private TableColumn<Shift, LocalTime> beginColumn, endColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final ShiftsTables shiftsTables = new ShiftsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(idBox)) {
            var shift = shiftsTables.findByCode(idBox.getValue());
            if (shift.isPresent()) {
                final ObservableList<Shift> list = FXCollections.observableArrayList(shift.get());
                CreateShiftView.create(table, idColumn, dayColumn, beginColumn, endColumn, unitColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idBox.getValue() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, shiftsTables, e -> e.getId().get(0));
    }
}
