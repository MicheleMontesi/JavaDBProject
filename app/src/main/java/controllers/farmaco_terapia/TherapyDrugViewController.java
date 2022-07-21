package controllers.farmaco_terapia;

import utilities.ConnectionProvider;
import db.tables.TherapyDrugsTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.TherapyDrug;
import utilities.views.CreateTherapyDrugView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class TherapyDrugViewController implements Initializable {
    @FXML
    private TableView<TherapyDrug> table;
    @FXML
    private TableColumn<TherapyDrug, Integer> therapyColumn, drugIdColumn, quantityColumn, consumptionColumn;
    @FXML
    private TableColumn<TherapyDrug, String> fiscalCodeColumn;
    @FXML
    private TableColumn<TherapyDrug, Date> dateColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final TherapyDrugsTable tdTable = new TherapyDrugsTable(connectionProvider.getMySQLConnection());

    private final ObservableList<TherapyDrug> list = FXCollections.observableArrayList(tdTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateTherapyDrugView.create(table, therapyColumn, consumptionColumn, dateColumn, quantityColumn,
                fiscalCodeColumn, drugIdColumn, list);
    }
}
