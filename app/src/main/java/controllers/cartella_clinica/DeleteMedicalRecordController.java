package controllers.cartella_clinica;

import db.tables.MedicalRecordsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import model.MedicalRecords;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteMedicalRecordController implements Initializable {
    @FXML
    private ChoiceBox<String> idBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final MedicalRecordsTables mrTable = new MedicalRecordsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (choiceBoxChecker(idBox)) {
            final var id = idBox.getValue();

            mrTable.delete(id);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(mrTable.findAll().stream().map(MedicalRecords::medicalRecordId).map(Objects::toString).distinct().toList());
        }
    }
}
