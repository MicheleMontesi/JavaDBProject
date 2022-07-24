package controllers.unita_operativa;

import db.tables.OperatingUnitTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.OperatingUnit;
import utilities.ConnectionProvider;
import utilities.views.CreateOperatingUnitView;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class FindOpUnitController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<OperatingUnit> table;
    @FXML
    private TableColumn<OperatingUnit, String> idColumn, typeColumn, nameColumn, locationColumn;
    @FXML
    private TableColumn<OperatingUnit, Integer> bedsColumn, patientsColumn;
    @FXML
    private TableColumn<OperatingUnit, Boolean> accreditColumn, authColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(idBox)) {
            var opUnit = operatingUnitTables.findByCode(idBox.getValue());
            if (opUnit.isPresent()) {
                final ObservableList<OperatingUnit> list = FXCollections.observableArrayList(opUnit.get());
                CreateOperatingUnitView.create(table, idColumn, typeColumn, nameColumn, locationColumn, bedsColumn, patientsColumn,
                        authColumn, accreditColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idBox.getValue() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, operatingUnitTables, e -> e.getId().get(0));
    }
}
