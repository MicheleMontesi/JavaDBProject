package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.WorkersTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Worker;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    @FXML
    private TableView<Worker> table;
    @FXML
    private TableColumn<Worker, String> idColumn, nameColumn, surnameColumn, residenceColumn, genderColumn, edQualColumn;
    @FXML
    private TableColumn<Worker, Date> birthColumn;
    @FXML
    private TableColumn<Worker, Integer> workerCodeColumn, ECMColumn;
    @FXML
    private TableColumn<Worker, Boolean> suitabilityColumn, partnerColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final WorkersTable workersTable = new WorkersTable(connectionProvider.getMySQLConnection());

    private final ObservableList<Worker> list = FXCollections.observableArrayList(workersTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
        residenceColumn.setCellValueFactory(new PropertyValueFactory<>("residence"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        workerCodeColumn.setCellValueFactory(new PropertyValueFactory<>("workerId"));
        edQualColumn.setCellValueFactory(new PropertyValueFactory<>("edQualification"));
        suitabilityColumn.setCellValueFactory(new PropertyValueFactory<>("suitability"));
        partnerColumn.setCellValueFactory(new PropertyValueFactory<>("partner"));
        ECMColumn.setCellValueFactory(new PropertyValueFactory<>("ECMCredits"));

        table.setItems(list);
    }
}
