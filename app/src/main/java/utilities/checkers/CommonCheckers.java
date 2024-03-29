package utilities.checkers;

import db.Table;
import db.tables.WorkersTables;
import javafx.scene.control.*;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class CommonCheckers {

    private static final Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    public static boolean isAlreadyPresent(List<?> idList, TextField field) {
        if (idList.contains(toUpperNormalizer(field))) {
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
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("The input \"" + field.getText() + "\" already exists");
                    errorAlert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean dateCheck(DatePicker datePicker) {
        errorAlert.setHeaderText("Input not valid");
        if (datePicker.getValue() == null) {
            errorAlert.setContentText("The input date must be filled");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static int getYearDifference(Date purchaseDate, Date expirationDate) {
        LocalDate newDate = new LocalDate(expirationDate);
        LocalDate oldDate = new LocalDate(purchaseDate);
        return Years.yearsBetween(oldDate, newDate).getYears();
    }

    public static boolean fieldChecker(List<Optional<? extends List<? extends Record>>> list) {
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
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input id or name doesn't exist");
        final var idCheck = workersTables.findByCode(id);
        final var nameCheck = ctTable.findByCode(name);
        return CommonCheckers.fieldChecker(List.of(idCheck, nameCheck));
    }

    public static void nonErasableCheck() {
        errorAlert.setHeaderText("The record can't be deleted");
        errorAlert.setContentText("The given record is used as foreign key from another record.\n" +
                "Delete it first and retry.");
        errorAlert.showAndWait();
    }
}
