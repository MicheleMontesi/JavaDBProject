package controllers.farmaco_terapia;

import db.tables.TherapiesTable;
import db.tables.TherapyDrugsTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.TherapyDrug;
import utilities.ConnectionProvider;
import utilities.views.CreateTherapyDrugView;

import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class ShowTherapyDrugByCodeController implements Initializable {
    @FXML
    private ChoiceBox<String> therapyIdBox, consumptionIdBox;
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
                choiceBoxChecker(therapyIdBox) &&
                choiceBoxChecker(consumptionIdBox)

        ) {
            var td = tdTable.findByParameters(Integer.parseInt(therapyIdBox.getValue()),
                    Integer.parseInt(consumptionIdBox.getValue()));
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
        if (therapyIdBox != null) {
            therapyIdBox.getItems().addAll(tdTable.findAll().stream().map(TherapyDrug::therapyId).map(Objects::toString).toList());
        }
        if (consumptionIdBox != null) {
            consumptionIdBox.getItems().addAll(tdTable.findAll().stream().map(TherapyDrug::consumptionId).map(Objects::toString).toList());
        }
    }
}
