package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.TurnsTable;
import db.tables.WorkersTable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Turn;
import model.Worker;
import utilities.CreateTurnView;

import java.time.LocalTime;

import static utilities.checkers.PersonCheckers.dayCheck;
import static utilities.checkers.PersonCheckers.lengthChecker;

public class WorkersTurnsController {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Turn> table;
    @FXML
    private TableColumn<Turn,String> dayColumn;
    @FXML
    private TableColumn<Turn, LocalTime> beginColumn, endColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final TurnsTable turnsTable = new TurnsTable(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var turn = turnsTable.findByFiscalCode(idField.getText());
            if (turn.isPresent()) {
                final ObservableList<Turn> list = FXCollections.observableArrayList(turn.get());
                CreateTurnView.create(table, dayColumn, beginColumn, endColumn, list);
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
