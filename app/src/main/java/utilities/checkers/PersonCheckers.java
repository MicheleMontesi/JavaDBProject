package utilities.checkers;

import db.Table;
import javafx.scene.control.*;
import model.PersonRelated;

import java.text.Normalizer;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class PersonCheckers {

    private static final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    public static boolean lengthChecker(TextInputControl field, int minLength, int maxLength) {
        if (field.getText().length() < minLength || field.getText().length() > maxLength) {
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
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The input \"" + field.getId() + "\" already exists");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean intCheck(TextField field, int minLength, int maxLength) {
        if (!(field.getText().matches("[0-9]*")) || field.getText().length() < minLength || field.getText().length() > maxLength) {
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
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The size of the field \"Sesso\" must be 1 character, M or F");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean birthAndCheck(DatePicker birthPicker, List<CheckBox> checkList) {
        errorAlert.setHeaderText("Input not valid");
        if (birthPicker.getValue() != null) {
            final Date birth = Date.from(birthPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            var differenceInYears = ((new Date().getTime() - birth.getTime())/(1000*3600*24))/365;
            if (differenceInYears > 80 || differenceInYears < 18) {
                errorAlert.setContentText("The date must be between 80 years ago and 18 years ago");
                errorAlert.showAndWait();
                return false;
            }
            for (var check : checkList) {
                if (!check.isSelected()) {
                    errorAlert.setContentText("The field \"" + check.getId() + "\" must be checked");
                    errorAlert.showAndWait();
                    return false;
                }
            }
            return true;
        } else {
            errorAlert.setContentText("The data must be between 80 years ago and 18 years ago");
            errorAlert.showAndWait();
            return false;
        }
    }

    public static boolean dayCheck(TextField dayField) {
        List<String> week = new ArrayList<>(Arrays.asList("LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "SABATO", "DOMENICA"));
        final var str = Normalizer.normalize(dayField.getText(), Normalizer.Form.NFD)
                .replaceAll("'", "")
                .replaceAll("[^\\p{ASCII}]", "")
                .toUpperCase();
        if (week.stream().noneMatch(e->e.equalsIgnoreCase(str))) {
            errorAlert.setHeaderText("Input Not Valid");
            errorAlert.setContentText("The day of the week must be one of these:\n" + week);
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean timeCheck(TextField timeField) {
        Pattern p = Pattern.compile("^([0-1]?\\d|2[0-3]):[0-5]\\d$");
        if (!p.matcher(timeField.getText()).matches()) {
            errorAlert.setHeaderText("Input Not Valid");
            errorAlert.setContentText("The time must be in the format HH:mm");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static String toUpperNormalizer(TextField field) {
        return Normalizer.normalize(field.getText(), Normalizer.Form.NFD)
                .replaceAll("'", "")
                .replaceAll("[^\\p{ASCII}]", "")
                .toUpperCase();
    }
}
