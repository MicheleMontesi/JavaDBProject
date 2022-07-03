package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.WorkersTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Worker;
import utilities.views.CreateWorkerView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FXUtils.disableOnWrite;
import static utilities.checkers.PersonCheckers.lengthChecker;

public class FindWorkerController implements Initializable {

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
    private TableColumn<Worker, Integer> workerIdColumn, ECMColumn;
    @FXML
    private TableColumn<Worker, Boolean> suitabilityColumn, partnerColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final WorkersTables workersTable = new WorkersTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var worker = workersTable.findByCode(idField.getText());
            if (worker.isPresent()) {
                final ObservableList<Worker> list = FXCollections.observableArrayList(worker.get());
                CreateWorkerView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                        workerIdColumn, suitabilityColumn, partnerColumn, edQualColumn, ECMColumn, list);
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
