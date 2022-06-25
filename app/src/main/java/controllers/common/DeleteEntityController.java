package controllers.common;

import db.ConnectionProvider;
import db.tables.WorkersTable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import static utilities.checkers.PersonCheckers.lengthChecker;

public class DeleteEntityController {

    @FXML
    private TextField idField;
    @FXML
    private Button deleteButton;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final WorkersTable workersTable = new WorkersTable(connectionProvider.getMySQLConnection());

    public void delete() {
        if (lengthChecker(idField, 16, 16)) {
            final String id = idField.getText();

            workersTable.delete(id);
        }
    }
}
