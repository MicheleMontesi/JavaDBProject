package utilities;

import db.Table;
import db.tables.CapitalGoodsTables;
import javafx.scene.control.ChoiceBox;
import model.CapitalGood;
import model.Entity;

import java.util.Objects;
import java.util.function.Function;

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

    public static void getList(ChoiceBox<String> box, Table<? extends Entity, String> table, Function<Entity, String> function) {
        if (box != null) {
            box.getItems().addAll(table.findAll().stream().map(function).distinct().toList());
        }
    }
}
