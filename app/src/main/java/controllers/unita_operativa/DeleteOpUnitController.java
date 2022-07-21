package controllers.unita_operativa;

import utilities.ConnectionProvider;
import db.tables.OperatingUnitTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.lengthChecker;

public class DeleteOpUnitController {

    @FXML
    private TextField idField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (lengthChecker(idField, 1, 5)) {
            final String id = idField.getText();

            operatingUnitTables.delete(id);
        }
    }
}
