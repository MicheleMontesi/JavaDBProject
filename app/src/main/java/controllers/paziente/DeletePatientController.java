package controllers.paziente;

import utilities.ConnectionProvider;
import db.tables.PatientsTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.lengthChecker;

public class DeletePatientController {

    @FXML
    private TextField idField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (lengthChecker(idField, 16, 16)) {
            final String id = idField.getText();

            patientsTables.delete(id);
        }
    }
}
