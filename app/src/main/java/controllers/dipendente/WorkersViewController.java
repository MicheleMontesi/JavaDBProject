package controllers.dipendente;

import utilities.ConnectionProvider;
import db.tables.WorkersTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Worker;
import utilities.views.CreateWorkerView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class WorkersViewController implements Initializable {
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

    private final ObservableList<Worker> list = FXCollections.observableArrayList(workersTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateWorkerView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                workerIdColumn, suitabilityColumn, partnerColumn, edQualColumn, ECMColumn, list);
    }
}
