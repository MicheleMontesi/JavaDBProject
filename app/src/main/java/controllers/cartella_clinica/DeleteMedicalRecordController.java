package controllers.cartella_clinica;

import utilities.ConnectionProvider;
import db.tables.MedicalRecordsTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class DeleteMedicalRecordController {
    @FXML
    private TextField idField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final MedicalRecordsTables mrTable = new MedicalRecordsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (lengthChecker(idField, 16, 16)) {
            final var id = toUpperNormalizer(idField);

            mrTable.delete(id);
        }
    }
}
