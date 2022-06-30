package utilities.checkers;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.List;

import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class CommonCheckers {

    public static boolean isAlreadyPresent(List<?> idList, TextField field) {
        if (idList.contains(toUpperNormalizer(field))) {
            final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The input \"" + field.getText() + "\" already exists");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }
}
