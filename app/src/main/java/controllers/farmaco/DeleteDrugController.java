package controllers.farmaco;

import db.tables.DrugsTables;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteDrugController {

    @FXML
    private ChoiceBox<String> idBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final DrugsTables drugsTables = new DrugsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (choiceBoxChecker(idBox)) {
            final String id = idBox.getValue();

            drugsTables.delete(id);
        }
    }
}
