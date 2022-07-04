package controllers.beni_strumentali;

import db.ConnectionProvider;
import db.tables.CapitalGoodsTables;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.CapitalGood;
import utilities.views.CreateCapitalGoodsView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.*;

public class ShowGoodByCodeController implements Initializable {
    @FXML
    private TextField unitIdField, goodIdField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<CapitalGood> table;
    @FXML
    private TableColumn<CapitalGood, String> toolColumn, plateColumn, typeColumn, unitIdColumn;
    @FXML
    private TableColumn<CapitalGood, Integer> goodIdColumn;
    @FXML
    private TableColumn<CapitalGood, Date> purchaseColumn, maintenanceColumn, expirationColumn;
    @FXML
    private TableColumn<CapitalGood, Boolean> vehicleColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final CapitalGoodsTables cgTables = new CapitalGoodsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (
                lengthChecker(unitIdField, 1, 5) &
                intCheck(goodIdField, 1, 5)

        ) {
            var good = cgTables.findByParameters(toUpperNormalizer(unitIdField),
                    Integer.parseInt(goodIdField.getText()));
            if (good.isPresent()) {
                final ObservableList<CapitalGood> list = FXCollections.observableArrayList(good.get());
                CreateCapitalGoodsView.create(table, unitIdColumn, goodIdColumn, purchaseColumn, maintenanceColumn,
                        vehicleColumn, toolColumn, plateColumn, typeColumn, expirationColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The input data doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding idFieldValid = Bindings.createBooleanBinding(() ->
                unitIdField.getText().isEmpty() || goodIdField.getText().isEmpty(),
                unitIdField.textProperty(), goodIdField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
