package controllers.dipendente;

import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import model.Worker;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteWorkerController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final WorkersTables workersTable = new WorkersTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (choiceBoxChecker(idBox)) {
            final String id = idBox.getValue();
            workersTable.delete(id);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(workersTable.findAll().stream().map(Worker::fiscalCode).map(Objects::toString).distinct().toList());
        }
    }
}
