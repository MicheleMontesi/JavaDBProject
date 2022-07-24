package controllers.tipologia_contratto;

import db.tables.ContractTypeTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteContractTypeController implements Initializable {

    @FXML
    private ChoiceBox<String> nameBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final ContractTypeTables ctTable = new ContractTypeTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (choiceBoxChecker(nameBox)) {
            final String id = nameBox.getValue();

            ctTable.delete(id);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(nameBox, ctTable, e -> e.getId().get(0));
    }
}
