package controllers.farmaco;

import utilities.ConnectionProvider;
import db.tables.DrugsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Drug;
import utilities.views.CreateDrugView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FXUtils.disableOnWrite;
import static utilities.checkers.PersonCheckers.intCheck;

public class FindDrugController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
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

    public void search() {
        if (intCheck(idField, 1, 10)) {
            var drug = drugsTables.findByCode(idField.getText());
            if (drug.isPresent()) {
                final ObservableList<Drug> list = FXCollections.observableArrayList(drug.get());
                CreateDrugView.create(table, idColumn, nameColumn, pharmaHouseColumn, purchaseColumn, expirationColumn, quantityColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idField.getText() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableOnWrite(searchButton, idField);
    }
}
