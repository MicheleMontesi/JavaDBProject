package controllers.tipologia_contratto;

import utilities.ConnectionProvider;
import db.tables.ContractTypeTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.ContractType;
import utilities.FXUtils;
import utilities.views.CreateContractTypeView;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.*;

public class ContractTypeSearchCodeController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<ContractType> table;
    @FXML
    private TableColumn<ContractType, String> nameColumn;
    @FXML
    private TableColumn<ContractType, Integer> hoursColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final ContractTypeTables ctTable = new ContractTypeTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(nameField, 1, 20)) {
            var ct = ctTable.findByCode(toUpperNormalizer(nameField));
            if (ct.isPresent()) {
                final ObservableList<ContractType> list = FXCollections.observableArrayList(ct.get());
                CreateContractTypeView.create(table, nameColumn, hoursColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + nameField.getText() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXUtils.disableOnWrite(searchButton, nameField);
    }
}
