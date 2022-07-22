package controllers.cartella_clinica;

import db.tables.MedicalRecordsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.MedicalRecords;
import utilities.ConnectionProvider;
import utilities.views.CreateMedicalRecordsView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class ShowMedicalRecordByCodeController implements Initializable {
    @FXML
    private ChoiceBox<String> idBox;
    @FXML
    private TableView<MedicalRecords> table;
    @FXML
    private TableColumn<MedicalRecords, Integer> medicalIdColumn;
    @FXML
    private TableColumn<MedicalRecords, String> diagnosisColumn, rehabColumn, anamnesisColumn, fiscalCodeColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final MedicalRecordsTables mrTable = new MedicalRecordsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(idBox)) {
            var mr = mrTable.findByCode(idBox.getValue());
            if (mr.isPresent()) {
                final ObservableList<MedicalRecords> list = FXCollections.observableArrayList(mr.get());
                CreateMedicalRecordsView.create(table, medicalIdColumn, fiscalCodeColumn, anamnesisColumn, diagnosisColumn,
                        rehabColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The input data doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(mrTable.findAll().stream().map(MedicalRecords::medicalRecordId).map(Objects::toString).distinct().toList());
        }
    }
}
