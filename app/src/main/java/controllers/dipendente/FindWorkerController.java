package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.WorkersTable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Worker;
import utilities.CreateView;

import java.util.Date;
import java.util.Optional;

import static utilities.checkers.PersonCheckers.lengthChecker;

public class FindWorkerController {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Worker> table;
    @FXML
    private TableColumn<Worker, String> idColumn, nameColumn, surnameColumn, residenceColumn, genderColumn, edQualColumn;
    @FXML
    private TableColumn<Worker, Date> birthColumn;
    @FXML
    private TableColumn<Worker, Integer> workerCodeColumn, ECMColumn;
    @FXML
    private TableColumn<Worker, Boolean> suitabilityColumn, partnerColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final WorkersTable workersTable = new WorkersTable(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var mar = workersTable.findByFiscalCode(idField.getText());
            if (mar.isPresent()) {
                final ObservableList<Worker> list = FXCollections.observableArrayList(mar.get());
                CreateView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                        workerCodeColumn, suitabilityColumn, partnerColumn, edQualColumn, ECMColumn, list);
            } else {
                System.out.println(Optional.empty());
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
