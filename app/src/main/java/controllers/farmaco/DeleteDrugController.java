package controllers.farmaco;

import db.tables.DrugsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteDrugController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, drugsTables, e -> e.getId().get(0));
    }
}
