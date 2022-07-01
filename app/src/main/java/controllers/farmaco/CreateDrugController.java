package controllers.farmaco;

import db.ConnectionProvider;
import db.Table;
import db.tables.DrugsTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Drug;
import model.SignedContract;
import utilities.checkers.CommonCheckers;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static utilities.checkers.PersonCheckers.*;

public class CreateDrugController {
    @FXML
    public TextField idField, nameField, companyField, quantityField;
    @FXML
    public DatePicker purchasePicker, expirationPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final DrugsTables drugsTables = new DrugsTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                intCheck(idField, 1, 10) &
                lengthChecker(nameField, 2, 20) &
                lengthChecker(companyField, 2, 50) &
                dateCheck(drugsTables, purchasePicker, expirationPicker) &
                intCheck(quantityField, 1, 5)
        ) {
            var id = Integer.parseInt(idField.getText());
            var name = toUpperNormalizer(nameField);
            var company = toUpperNormalizer(companyField);
            var purchase = Date.from(Instant.from(purchasePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var expiration = Date.from(Instant.from(expirationPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var quantity = Integer.parseInt(quantityField.getText());

            drugsTables.save(new Drug(id, name, company, purchase, expiration, quantity));
        }
    }

    private boolean dateCheck(Table<Drug, String> table, DatePicker purchasePicker, DatePicker expirationPicker) {
        var list = table.findAll();
        var purchaseDate = Date.from(Instant.from(purchasePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        var expirationDate = Date.from(Instant.from(expirationPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        if (getYearDifference(purchaseDate, expirationDate) <= 0) {
            errorAlert.setContentText("The input expiration date must be one year bigger than the purchase date");
            errorAlert.showAndWait();
            return false;
        }

        for (var drug : list) {
            if (drug.drugId() == Integer.parseInt(idField.getText())) {
                errorAlert.setContentText("The input code already exists, update it instead");
                errorAlert.showAndWait();
            }
        }
        return true;
    }

    private int getYearDifference(Date purchaseDate, Date expirationDate) {
        Calendar newDate = getCalendar(expirationDate);
        Calendar oldDate = getCalendar(purchaseDate);
        return CommonCheckers.getYears(newDate, oldDate);
    }

    private Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.setTime(date);
        return cal;
    }
}
