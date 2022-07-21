package controllers.contratto_stipulato;

import utilities.ConnectionProvider;
import db.tables.SignedContractsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.SignedContract;
import utilities.views.CreateSignedContractView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class SignedContractViewController implements Initializable {
    @FXML
    private TableView<SignedContract> table;
    @FXML
    private TableColumn<SignedContract, String> fiscalCodeColumn, nameColumn;
    @FXML
    private TableColumn<SignedContract, Date> endColumn, stipulationColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final SignedContractsTables scTable = new SignedContractsTables(connectionProvider.getMySQLConnection());

    private final ObservableList<SignedContract> list = FXCollections.observableArrayList(scTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateSignedContractView.create(table, fiscalCodeColumn, stipulationColumn, endColumn, nameColumn, list);
    }
}
