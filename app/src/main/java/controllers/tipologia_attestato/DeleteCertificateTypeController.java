package controllers.tipologia_attestato;

import db.ConnectionProvider;
import db.tables.TherapiesTable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.lengthChecker;

public class DeleteCertificateTypeController {

    @FXML
    private TextField nameField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());

    public void delete() {
        if (lengthChecker(nameField, 16, 16)) {
            final String id = nameField.getText();

            therapiesTable.delete(id);
        }
    }
}
