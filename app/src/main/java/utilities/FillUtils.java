package utilities;

import db.tables.CapitalGoodsTables;
import javafx.scene.control.ChoiceBox;
import model.CapitalGood;

import java.util.Objects;

public class FillUtils {
    public static void fillGoodField(ChoiceBox<String> unitBox, ChoiceBox<String> goodBox, CapitalGoodsTables cgTable) {
        if (!unitBox.getSelectionModel().isEmpty()) {
            if (goodBox != null) {
                goodBox.getItems().removeAll(goodBox.getItems());
                goodBox.getItems().addAll(cgTable.findAll().stream()
                        .filter(e -> e.unitId().equals(unitBox.getValue()))
                        .map(CapitalGood::goodId).map(Objects::toString).toList());
            }
        }
    }
}
