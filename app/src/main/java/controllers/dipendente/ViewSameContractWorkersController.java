package controllers.dipendente;

import db.tables.ContractTypeTables;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class ViewSameContractWorkersController implements Initializable {

    @FXML
    private ChoiceBox<String> contractBox;
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
    private final ContractTypeTables ctTable = new ContractTypeTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(contractBox)) {
            var worker = workersTable.findByContract(contractBox.getValue());
            if (worker.isPresent()) {
                final ObservableList<Worker> list = FXCollections.observableArrayList(worker.get().stream().distinct().toList());
                CreateWorkerView.create(table, idColumn, nameColumn, surnameColumn, birthColumn, residenceColumn, genderColumn,
                        workerIdColumn, suitabilityColumn, partnerColumn, edQualColumn, ECMColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("Nobody has this contract:  \"" + contractBox.getValue() + "\"");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(contractBox, ctTable, e -> e.getId().get(0));
    }
}
