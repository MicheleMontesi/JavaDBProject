package controllers.farmaco;

import db.ConnectionProvider;
import db.tables.DrugsTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.lengthChecker;

public class DeleteDrugController {

    @FXML
    private TextField idField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final DrugsTables drugsTables = new DrugsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (lengthChecker(idField, 16, 16)) {
            final String id = idField.getText();

            drugsTables.delete(id);
        }
    }
}
