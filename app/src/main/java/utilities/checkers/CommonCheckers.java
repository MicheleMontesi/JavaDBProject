package utilities.checkers;

import db.Table;
import db.tables.WorkersTables;
import javafx.scene.control.*;

import java.util.*;

import static java.util.Calendar.*;
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

    public static boolean isOptionalAlreadyPresent(List<Optional<String>> idList, TextField field) {
        for (var o : idList) {
            if (o.isPresent()) {
                if(o.get().equals(toUpperNormalizer(field))) {
                    final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("The input \"" + field.getText() + "\" already exists");
                    errorAlert.showAndWait();
                    return false;
                }
            }
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

    public static int getYearDifference(Date purchaseDate, Date expirationDate) {
        Calendar newDate = getCalendar(expirationDate);
        Calendar oldDate = getCalendar(purchaseDate);
        return CommonCheckers.getYears(newDate, oldDate);
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.setTime(date);
        return cal;
    }

    public static boolean fieldChecker(List<Optional<? extends List<? extends Record>>> list) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input doesn't exist");

        for (var e : list) {
            if (e.isEmpty()) {
                errorAlert.showAndWait();
                return false;
            } else if (e.get().size() == 0) {
                errorAlert.showAndWait();
                return false;
            }
        }
        return true;
    }

    public static boolean checkChecker(CheckBox check) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The check box " + check.getId() + " must be selected");
        if (!check.isSelected()) {
            errorAlert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    public static boolean choiceBoxChecker(ChoiceBox<String> box) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The choice box " + box.getId() + " must be selected");
        if (box.getSelectionModel().isEmpty()) {
            errorAlert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkExistence(WorkersTables workersTables, String id, Table<? extends Record, String> ctTable, String name) {
        final var idCheck = workersTables.findByCode(id);
        final var nameCheck = ctTable.findByCode(name);
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input id or name doesn't exist");
        return CommonCheckers.fieldChecker(List.of(idCheck, nameCheck));
    }
}
