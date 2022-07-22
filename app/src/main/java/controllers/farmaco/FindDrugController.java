package controllers.farmaco;

import db.tables.DrugsTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Drug;
import utilities.ConnectionProvider;
import utilities.views.CreateDrugView;

import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class FindDrugController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;
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
        if (choiceBoxChecker(idBox)) {
            var drug = drugsTables.findByCode(idBox.getValue());
            if (drug.isPresent()) {
                final ObservableList<Drug> list = FXCollections.observableArrayList(drug.get());
                CreateDrugView.create(table, idColumn, nameColumn, pharmaHouseColumn, purchaseColumn, expirationColumn, quantityColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idBox.getValue() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(drugsTables.findAll().stream().map(Drug::drugId).map(Objects::toString).distinct().toList());
        }
    }
}
