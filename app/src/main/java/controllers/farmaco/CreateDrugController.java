package controllers.farmaco;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import model.Worker;
import utilities.ConnectionProvider;
import db.Table;
import db.tables.DrugsTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Drug;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.getYearDifference;
import static utilities.checkers.PersonCheckers.*;

public class CreateDrugController implements Initializable {
    @FXML
    public TextField idField, nameField, companyField, quantityField;
    @FXML
    public DatePicker purchasePicker, expirationPicker;
    @FXML
    private ChoiceBox<String> idBox;
    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final DrugsTables drugsTables = new DrugsTables(connectionProvider.getMySQLConnection());

    private int id, quantity;
    private String name, company;
    private Date purchase, expiration;

    public void create() {
        if (check() &&
            isNotAlreadyPresent(drugsTables) &&
            intCheck(idField, 1, 10)) {
            this.init();
            id = Integer.parseInt(idField.getText());
            drugsTables.save(new Drug(id, name, company, purchase, expiration, quantity));
        }
    }

    public void update() {
        if (check()) {
            this.init();
            id = Integer.parseInt(idBox.getValue());
            drugsTables.update(new Drug(id, name, company, purchase, expiration, quantity));
        }
    }

    private boolean check() {
        return lengthChecker(nameField, 2, 20) &
                lengthChecker(companyField, 2, 50) &
                dateCheck(purchasePicker, expirationPicker) &
                intCheck(quantityField, 1, 5);
    }

    private void init() {

        name = toUpperNormalizer(nameField);
        company = toUpperNormalizer(companyField);
        purchase = Date.from(Instant.from(purchasePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        expiration = Date.from(Instant.from(expirationPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        quantity = Integer.parseInt(quantityField.getText());
    }

    private boolean dateCheck(DatePicker purchasePicker, DatePicker expirationPicker) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        if (purchasePicker.getValue() != null & expirationPicker.getValue() != null) {
            var purchaseDate = Date.from(Instant.from(purchasePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var expirationDate = Date.from(Instant.from(expirationPicker.getValue().atStartOfDay(ZoneId.systemDefault())));

            if (getYearDifference(purchaseDate, expirationDate) <= 0) {
                errorAlert.setContentText("The input expiration date must be one year bigger than the purchase date");
                errorAlert.showAndWait();
                return false;
            }
            return true;
        } else {
            errorAlert.setContentText("The input dates must be filled");
            errorAlert.showAndWait();
            return false;
        }
    }

    private boolean isNotAlreadyPresent(Table<Drug, String> table) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        var list = table.findAll();
        for (var drug : list) {
            if (drug.drugId() == Integer.parseInt(idField.getText())) {
                errorAlert.setContentText("The input code already exists, update it instead");
                errorAlert.showAndWait();
                return false;
            }
        }
        return true;
    }

    public void fillFields() {
        if (!idBox.getSelectionModel().isEmpty()) {
            var selected = idBox.getSelectionModel().getSelectedItem();
            var drugList = drugsTables.findByCode(selected);
            if (drugList.isPresent()) {
                var drug = drugList.get().stream().findFirst().orElse(null);
                if (drug != null) {
                    nameField.setText(drug.name());
                    companyField.setText(drug.pharmaCompany());
                    purchasePicker.getEditor().setText(drug.purchaseDate().toString());
                    expirationPicker.getEditor().setText(drug.expirationDate().toString());
                    quantityField.setText(Objects.toString(drug.quantity()));
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(drugsTables.findAll().stream().map(Drug::drugId).map(Objects::toString).distinct().toList());
        }
    }
}
