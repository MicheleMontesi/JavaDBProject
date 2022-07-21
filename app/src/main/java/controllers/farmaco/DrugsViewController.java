package controllers.farmaco;

import utilities.ConnectionProvider;
import db.tables.DrugsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Drug;
import utilities.views.CreateDrugView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class DrugsViewController implements Initializable {
    @FXML
    private TableView<Drug> table;
    @FXML
    private TableColumn<Drug, String> nameColumn, pharmaHouseColumn;
    @FXML
    private TableColumn<Drug, Date> purchaseColumn, expirationColumn;
    @FXML
    private TableColumn<Drug, Integer> idColumn, quantityColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final DrugsTables drugsTables = new DrugsTables(connectionProvider.getMySQLConnection());

    private final ObservableList<Drug> list = FXCollections.observableArrayList(drugsTables.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateDrugView.create(table, idColumn, nameColumn, pharmaHouseColumn, purchaseColumn, expirationColumn, quantityColumn, list);
    }
}
