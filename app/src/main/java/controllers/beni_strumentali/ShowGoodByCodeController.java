package controllers.beni_strumentali;

import db.tables.CapitalGoodsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.CapitalGood;
import utilities.ConnectionProvider;
import utilities.FillUtils;
import utilities.views.CreateCapitalGoodsView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class ShowGoodByCodeController implements Initializable {
    @FXML
    private ChoiceBox<String> unitBox, goodBox;
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

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final CapitalGoodsTables cgTables = new CapitalGoodsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (
                choiceBoxChecker(unitBox) &&
                choiceBoxChecker(goodBox)

        ) {
            var good = cgTables.findByParameters(unitBox.getValue(),
                    Integer.parseInt(goodBox.getValue()));
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

    public void fillGoodField() {
        FillUtils.fillGoodField(unitBox, goodBox, cgTables);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (unitBox != null) {
            unitBox.getItems().addAll(cgTables.findAll().stream().map(CapitalGood::unitId).distinct().toList());
        }
    }
}
