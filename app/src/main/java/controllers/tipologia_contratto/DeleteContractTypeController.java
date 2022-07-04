package controllers.tipologia_contratto;

import db.ConnectionProvider;
import db.tables.ContractTypeTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.lengthChecker;

public class DeleteContractTypeController {

    @FXML
    private TextField nameField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ContractTypeTables ctTable = new ContractTypeTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (lengthChecker(nameField, 1, 20)) {
            final String id = nameField.getText();

            ctTable.delete(id);
        }
    }
}
