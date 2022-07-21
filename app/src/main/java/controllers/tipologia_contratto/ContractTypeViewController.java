package controllers.tipologia_contratto;

import utilities.ConnectionProvider;
import db.tables.ContractTypeTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.ContractType;
import utilities.views.CreateContractTypeView;

import java.net.URL;
import java.util.ResourceBundle;

public class ContractTypeViewController implements Initializable {

    @FXML
    private TableView<ContractType> table;
    @FXML
    private TableColumn<ContractType, Integer> hoursColumn;
    @FXML
    private TableColumn<ContractType, String> nameColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final ContractTypeTables ctTable = new ContractTypeTables(connectionProvider.getMySQLConnection());

    private final ObservableList<ContractType> list = FXCollections.observableArrayList(ctTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateContractTypeView.create(table, nameColumn, hoursColumn, list);
    }
}
