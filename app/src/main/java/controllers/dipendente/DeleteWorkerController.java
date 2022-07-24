package controllers.dipendente;

import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
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
        getList(idBox, workersTable, e -> e.getId().get(0));
    }
}
