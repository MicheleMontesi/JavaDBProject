package controllers.dipendente;

import db.tables.WorkersTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Worker;
import utilities.ConnectionProvider;
import utilities.views.CreateWorkerView;

import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class FindWorkerController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;
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

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final WorkersTables workersTable = new WorkersTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(idBox)) {
            var worker = workersTable.findByCode(idBox.getValue());
            if (worker.isPresent()) {
                final ObservableList<Worker> list = FXCollections.observableArrayList(worker.get());
                CreateWorkerView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                        workerIdColumn, suitabilityColumn, partnerColumn, edQualColumn, ECMColumn, list);
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
        if (idBox != null) {
            idBox.getItems().addAll(workersTable.findAll().stream().map(Worker::fiscalCode).map(Objects::toString).distinct().toList());
        }
    }
}
