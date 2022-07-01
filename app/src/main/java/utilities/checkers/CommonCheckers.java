package utilities.checkers;

import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.Calendar;
import java.util.List;

import static java.util.Calendar.*;
import static java.util.Calendar.DATE;
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

    public static int getYears(Calendar newDate, Calendar oldDate) {
        int diff = newDate.get(YEAR) - oldDate.get(YEAR);
        if (newDate.get(MONTH) > oldDate.get(MONTH) ||
                (newDate.get(MONTH) == oldDate.get(MONTH) && newDate.get(DATE) > oldDate.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static boolean dateCheck(DatePicker datePicker) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        if (datePicker.getValue() == null) {
            errorAlert.setContentText("The input date must be filled");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }
}
