package utilities;

import db.Table;
import javafx.scene.control.ChoiceBox;
import model.Entity;

import java.util.Objects;
import java.util.function.Function;

public class FillUtils {
    public static void fillRelatedField(ChoiceBox<String> firstBox, ChoiceBox<String> secondBox,
                                        Table<? extends Entity, String> table, int firstIndex, int secondIndex) {
        if (!firstBox.getSelectionModel().isEmpty()) {
            if (secondBox != null) {
                secondBox.getItems().removeAll(secondBox.getItems());
                secondBox.getItems().addAll(table.findAll().stream()
                        .filter(e -> e.getId().get(firstIndex).equals(firstBox.getValue()))
                        .map(e -> e.getId().get(secondIndex)).map(Objects::toString).toList());
            }
        }
    }

    public static void getList(ChoiceBox<String> box, Table<? extends Entity, String> table, Function<Entity, String> function) {
        if (box != null) {
            box.getItems().addAll(table.findAll().stream().map(function).distinct().toList());
        }
    }
}
