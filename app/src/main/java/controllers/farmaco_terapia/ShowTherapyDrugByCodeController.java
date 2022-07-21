package controllers.farmaco_terapia;

import utilities.ConnectionProvider;
import db.tables.TherapyDrugsTable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.TherapyDrug;
import utilities.views.CreateTherapyDrugView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.*;

public class ShowTherapyDrugByCodeController implements Initializable {
    @FXML
    private TextField therapyIdField, consumptionIdField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<TherapyDrug> table;
    @FXML
    private TableColumn<TherapyDrug, Integer> therapyColumn, drugIdColumn, quantityColumn, consumptionColumn;
    @FXML
    private TableColumn<TherapyDrug, String> fiscalCodeColumn;
    @FXML
    private TableColumn<TherapyDrug, Date> dateColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final TherapyDrugsTable tdTable = new TherapyDrugsTable(connectionProvider.getMySQLConnection());

    public void search() {
        if (
                intCheck(therapyIdField, 1, 10) &
                intCheck(consumptionIdField, 1, 6)

        ) {
            var td = tdTable.findByParameters(Integer.parseInt(therapyIdField.getText()),
                    Integer.parseInt(consumptionIdField.getText()));
            if (td.isPresent()) {
                final ObservableList<TherapyDrug> list = FXCollections.observableArrayList(td.get());
                CreateTherapyDrugView.create(table, therapyColumn, consumptionColumn, dateColumn, quantityColumn,
                        fiscalCodeColumn, drugIdColumn, list);
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
                therapyIdField.getText().isEmpty() || consumptionIdField.getText().isEmpty(),
                therapyIdField.textProperty(), consumptionIdField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
