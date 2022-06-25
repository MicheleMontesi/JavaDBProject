package utilities.checkers;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.ZoneId;
import java.util.Date;

public class PersonCheckers {

    public static boolean lengthChecker(TextField field, int minLength, int maxLength) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        if (field.getText().length() < minLength || field.getText().length() > maxLength) {
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The size of the field \"" + field.getId() + "\" must be between " + minLength +
                    " and " + maxLength + " characters");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean intCheck(TextField field, int minLength, int maxLength) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
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
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        if (!(genderField.getText().equalsIgnoreCase("M") || genderField.getText().equalsIgnoreCase("F"))) {
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
}