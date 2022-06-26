package utilities.checkers;

import db.Table;
import db.tables.WorkersTable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.PersonRelated;
import model.Worker;

import java.text.Normalizer;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class PersonCheckers {

    public static boolean lengthChecker(TextField field, int minLength, int maxLength) {
        if (field.getText().length() < minLength || field.getText().length() > maxLength) {
            final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The size of the field \"" + field.getId() + "\" must be between " + minLength +
                    " and " + maxLength + " characters");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean isNotAlreadyPresent(TextField field, Table<? extends PersonRelated, String> table, Function<PersonRelated, ?> matchString) {
        var list = table.findAll();
        List<?> idList = list.stream().map(matchString).toList();
        if (idList.contains(field.getText())) {
            final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The input \"" + field.getId() + "\" already exists");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean intCheck(TextField field, int minLength, int maxLength) {
        if (!(field.getText().matches("[0-9]*")) || field.getText().length() < minLength || field.getText().length() > maxLength) {
            final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The size of the field \"" + field.getId() + "\" must be between " + minLength +
                    " and " + maxLength + " digits (0-9)");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean genderCheck(TextField genderField) {
        if (!(genderField.getText().equalsIgnoreCase("M") || genderField.getText().equalsIgnoreCase("F"))) {
            final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The size of the field \"Sesso\" must be 1 character, M or F");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean birthAndSuitabilityCheck(DatePicker birthPicker, CheckBox suitabilityCheck) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        if (birthPicker.getValue() != null) {
            final Date birth = Date.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            var differenceInYears = ((new Date().getTime() - birth.getTime())/(1000*3600*24))/365;
            if (((differenceInYears > 80 || differenceInYears < 18) || !suitabilityCheck.isSelected())) {
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The date must be between 80 years ago and 18 years ago\n" +
                        "and the field \"IdonitaAllaMansione\" must be checked");
                errorAlert.showAndWait();
                return false;
            }
            return true;
        } else {
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The data must be between 80 years ago and 18 years ago\n" +
                    "and the field \"IdonitaAllaMansione\" must be checked");
            errorAlert.showAndWait();
            return false;
        }
    }

    public static boolean dayCheck(TextField dayField) {
        List<String> week = new ArrayList<>(Arrays.asList("LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "SABATO", "DOMENICA"));
        final var str = Normalizer.normalize(dayField.getText(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        if (week.stream().noneMatch(e->e.equalsIgnoreCase(str))) {
            final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("INPUT NOT VALID");
            errorAlert.setContentText("The day of the week must be one of these:\n" + week);
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean timeCheck(TextField timeField) {
        Pattern p = Pattern.compile("^([0-1]?\\d|2[0-3]):[0-5]\\d$");
        if (!p.matcher(timeField.getText()).matches()) {
            final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("INPUT NOT VALID");
            errorAlert.setContentText("The time must be in the format HH:mm");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }
}
