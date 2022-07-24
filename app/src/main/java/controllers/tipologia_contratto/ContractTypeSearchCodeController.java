package controllers.tipologia_contratto;

import db.tables.ContractTypeTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.ContractType;
import utilities.ConnectionProvider;
import utilities.views.CreateContractTypeView;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class ContractTypeSearchCodeController implements Initializable {

    @FXML
    private ChoiceBox<String> nameBox;
    @FXML
    private TableView<ContractType> table;
    @FXML
    private TableColumn<ContractType, String> nameColumn;
    @FXML
    private TableColumn<ContractType, Integer> hoursColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final ContractTypeTables ctTable = new ContractTypeTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(nameBox)) {
            var ct = ctTable.findByCode(nameBox.getValue());
            if (ct.isPresent()) {
                final ObservableList<ContractType> list = FXCollections.observableArrayList(ct.get());
                CreateContractTypeView.create(table, nameColumn, hoursColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + nameBox.getValue() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(nameBox, ctTable, e -> e.getId().get(0));
    }
}
