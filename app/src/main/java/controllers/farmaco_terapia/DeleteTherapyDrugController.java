package controllers.farmaco_terapia;

import db.tables.TherapyDrugsTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
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
        getList(therapyIdBox, tdTable, e -> e.getId().get(0));
        getList(consumptionIdBox, tdTable, e -> e.getId().get(1));
    }
}
