package controllers.beni_strumentali;

import db.ConnectionProvider;
import db.tables.CapitalGoodsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CapitalGood;
import utilities.views.CreateCapitalGoodsView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class CapitalGoodsViewController implements Initializable {
    @FXML
    private TableView<CapitalGood> table;
    @FXML
    private TableColumn<CapitalGood, String> unitIdColumn, toolColumn, typeColumn, plateColumn;
    @FXML
    private TableColumn<CapitalGood, Boolean> vehicleColumn;
    @FXML
    private TableColumn<CapitalGood, Date> maintenanceColumn, purchaseColumn, expirationColumn;
    @FXML
    private TableColumn<CapitalGood, Integer> goodIdColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final CapitalGoodsTables cgTable = new CapitalGoodsTables(connectionProvider.getMySQLConnection());

    private final ObservableList<CapitalGood> list = FXCollections.observableArrayList(cgTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateCapitalGoodsView.create(table, unitIdColumn, goodIdColumn, purchaseColumn, maintenanceColumn,
                vehicleColumn, toolColumn, plateColumn, typeColumn, expirationColumn, list);
    }
}
