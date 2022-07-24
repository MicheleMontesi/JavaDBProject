package controllers.terapia;

import db.tables.TherapiesTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteTherapyController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());

    public void delete() {
        if (choiceBoxChecker(idBox)) {
            final String id = idBox.getValue();

            therapiesTable.delete(id);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, therapiesTable, e -> e.getId().get(0));
    }
}
