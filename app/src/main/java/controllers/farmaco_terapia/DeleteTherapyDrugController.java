package controllers.farmaco_terapia;

import db.tables.TherapyDrugsTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import model.TherapyDrug;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteTherapyDrugController implements Initializable {

    @FXML
    private ChoiceBox<String> therapyIdBox, consumptionIdBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final TherapyDrugsTable tdTable = new TherapyDrugsTable(connectionProvider.getMySQLConnection());


    public void delete() {
        if (
                choiceBoxChecker(therapyIdBox) &&
                choiceBoxChecker(consumptionIdBox)
        ) {
            final var therapyId = Integer.parseInt(therapyIdBox.getValue());
            final var consumptionId = Integer.parseInt(consumptionIdBox.getValue());

            tdTable.deleteByParameters(therapyId, consumptionId);
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
