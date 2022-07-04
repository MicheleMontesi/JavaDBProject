package controllers.farmaco_terapia;

import db.ConnectionProvider;
import db.tables.TherapyDrugsTable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.intCheck;

public class DeleteTherapyDrugController {

    @FXML
    private TextField therapyIdField, consumptionIdField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final TherapyDrugsTable tdTable = new TherapyDrugsTable(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                intCheck(therapyIdField, 1, 10) &
                intCheck(consumptionIdField, 1, 6)
        ) {
            final var therapyId = Integer.parseInt(therapyIdField.getText());
            final var consumptionId = Integer.parseInt(consumptionIdField.getText());

            tdTable.deleteByParameters(therapyId, consumptionId);
        }
    }
}
