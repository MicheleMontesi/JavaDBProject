package controllers.cartella_clinica;

import utilities.ConnectionProvider;
import db.tables.MedicalRecordsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.MedicalRecords;
import utilities.views.CreateMedicalRecordsView;

import java.net.URL;
import java.util.ResourceBundle;

public class MedicalRecordsViewController implements Initializable {
    @FXML
    private TableView<MedicalRecords> table;
    @FXML
    private TableColumn<MedicalRecords, Integer> medicalIdColumn;
    @FXML
    private TableColumn<MedicalRecords, String> diagnosisColumn, rehabColumn, anamnesisColumn, fiscalCodeColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final MedicalRecordsTables mrTable = new MedicalRecordsTables(connectionProvider.getMySQLConnection());

    private final ObservableList<MedicalRecords> list = FXCollections.observableArrayList(mrTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateMedicalRecordsView.create(table, medicalIdColumn, fiscalCodeColumn, anamnesisColumn, diagnosisColumn,
                rehabColumn, list);
    }
}
