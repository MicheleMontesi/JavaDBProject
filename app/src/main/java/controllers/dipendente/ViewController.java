package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.WorkersTable;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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

        idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().fiscalCode()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name()));
        surnameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().surname()));
        birthColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(this, "", cellData.getValue().birthDay()));
        residenceColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().residence()));
        genderColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().gender()));
        workerCodeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().workerId()));
        edQualColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().edQualification()));
        suitabilityColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().suitability()));
        partnerColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().partner()));
        ECMColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().ECMCredits()));

        table.setItems(list);
    }
}
