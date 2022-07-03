package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import static utilities.checkers.PersonCheckers.lengthChecker;

public class DeleteWorkerController {

    @FXML
    private TextField idField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final WorkersTables workersTable = new WorkersTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (lengthChecker(idField, 16, 16)) {
            final String id = idField.getText();

            workersTable.delete(id);
        }
    }
}
