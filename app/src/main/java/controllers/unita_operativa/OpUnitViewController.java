package controllers.unita_operativa;

import db.ConnectionProvider;
import db.tables.OperatingUnitTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.OperatingUnit;
import utilities.views.CreateOperatingUnitView;

import java.net.URL;
import java.util.ResourceBundle;

public class OpUnitViewController implements Initializable {
    @FXML
    private TableView<OperatingUnit> table;
    @FXML
    private TableColumn<OperatingUnit, String> idColumn, typeColumn, nameColumn, locationColumn;
    @FXML
    private TableColumn<OperatingUnit, Integer> bedsColumn, patientsColumn;
    @FXML
    private TableColumn<OperatingUnit, Boolean> accreditColumn, authColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());

    private final ObservableList<OperatingUnit> list = FXCollections.observableArrayList(operatingUnitTables.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateOperatingUnitView.create(table, idColumn, typeColumn, nameColumn, locationColumn, bedsColumn, patientsColumn,
                authColumn, accreditColumn, list);
    }
}