package controllers.beni_strumentali;

import utilities.ConnectionProvider;
import db.tables.CapitalGoodsTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static utilities.checkers.PersonCheckers.*;

public class DeleteCapitalGoodController {

    @FXML
    private TextField unitIdField, goodIdField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final CapitalGoodsTables cgTable = new CapitalGoodsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                lengthChecker(unitIdField, 1, 5) &
                intCheck(goodIdField, 2, 5)
        ) {
            final var unitId = toUpperNormalizer(unitIdField);
            final var goodId = Integer.parseInt(goodIdField.getText());

            cgTable.deleteByParameters(unitId, goodId);
        }
    }
}
