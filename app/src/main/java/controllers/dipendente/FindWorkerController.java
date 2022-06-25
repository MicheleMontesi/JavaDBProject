package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.WorkersTable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Optional;

import static utilities.checkers.PersonCheckers.lengthChecker;

public class FindWorkerController {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final WorkersTable workersTable = new WorkersTable(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var mar = workersTable.findByFiscalCode(idField.getText());
            if (mar.isPresent()) {
                System.out.println(mar.get());
            } else {
                System.out.println(Optional.empty());
            }
        }
    }

    public void disableOnWrite() {
        BooleanBinding idFieldValid = Bindings.createBooleanBinding(() -> idField.getText().isEmpty(), idField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
