package controllers.beni_strumentali;

import db.tables.CapitalGoodsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;
import utilities.FillUtils;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteCapitalGoodController implements Initializable {

    @FXML
    private ChoiceBox<String> unitBox, goodBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final CapitalGoodsTables cgTable = new CapitalGoodsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                choiceBoxChecker(unitBox) &&
                choiceBoxChecker(goodBox)
        ) {
            final var unitId = unitBox.getValue();
            final var goodId = Integer.parseInt(goodBox.getValue());

            cgTable.deleteByParameters(unitId, goodId);
        }
    }

    public void fillGoodField() {
        FillUtils.fillRelatedField(unitBox, goodBox, cgTable, 0, 1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(unitBox, cgTable, e -> e.getId().get(0));
    }
}
