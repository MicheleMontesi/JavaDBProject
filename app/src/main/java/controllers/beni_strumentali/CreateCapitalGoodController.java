package controllers.beni_strumentali;

import db.ConnectionProvider;
import db.Table;
import db.tables.CapitalGoodsTables;
import db.tables.OperatingUnitTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.CapitalGood;
import utilities.checkers.CommonCheckers;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import static utilities.checkers.CommonCheckers.getYearDifference;
import static utilities.checkers.PersonCheckers.*;

public class CreateCapitalGoodController implements Initializable {
    @FXML
    public TextField unitField, goodField, toolField, plateField;
    @FXML
    public DatePicker purchasePicker, maintenancePicker, expirationPicker;
    @FXML
    public CheckBox vehicleCheck;
    @FXML
    public ChoiceBox<String> typeChoice;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final CapitalGoodsTables capitalGoodsTables = new CapitalGoodsTables(connectionProvider.getMySQLConnection());
    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                lengthChecker(unitField, 1, 5) &
                intCheck(goodField, 1, 5) &
                checkUnitExistence() &
                dateCheck(capitalGoodsTables, purchasePicker, maintenancePicker)
        ) {
            final var unitId = toUpperNormalizer(unitField);
            final var goodId = Integer.parseInt(goodField.getText());
            final var purchaseDate = Date.from(Instant.from(purchasePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final var maintenanceDate = Date.from(Instant.from(maintenancePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final var vehicle = vehicleCheck.isSelected();
            Optional<String> plate = Optional.empty();
            Optional<String> type = Optional.empty();
            Optional<Date> expirationDate = Optional.empty();
            Optional<String> toolName = Optional.empty();

            if (vehicleCheck.isSelected()) {
                if (
                        lengthChecker(plateField, 4, 10) &
                        plateNotAlreadyExists() &
                        CommonCheckers.getYearDifference(purchaseDate, Date.from(Instant.from(
                                expirationPicker.getValue().atStartOfDay(ZoneId.systemDefault())))) > 0
                ) {
                    plate = Optional.of(toUpperNormalizer(plateField));
                    type = Optional.of(typeChoice.getValue());
                    expirationDate = Optional.of(Date.from(
                            Instant.from(maintenancePicker.getValue().atStartOfDay(ZoneId.systemDefault()))));

                    capitalGoodsTables.save(new CapitalGood(unitId, goodId, purchaseDate, maintenanceDate, vehicle,
                            toolName, plate, type, expirationDate));
                }
            } else {
                if (lengthChecker(toolField, 2, 20)) {
                    toolName = Optional.of(toUpperNormalizer(toolField));
                    capitalGoodsTables.save(new CapitalGood(unitId, goodId, purchaseDate, maintenanceDate, vehicle,
                            toolName, plate, type, expirationDate));
                }
            }
        }
    }

    private boolean dateCheck(Table<CapitalGood, String> table, DatePicker purchasePicker, DatePicker maintenancePicker) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        if (purchasePicker.getValue() != null || maintenancePicker != null) {
            final var list = table.findAll();
            final var purchaseDate = Date.from(Instant.from(purchasePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final var maintenanceDate = Date.from(Instant.from(maintenancePicker.getValue().atStartOfDay(ZoneId.systemDefault())));

            if (getYearDifference(purchaseDate, maintenanceDate) < 0) {
                errorAlert.setContentText("The input maintenance date must be one year bigger than the purchase date");
                errorAlert.showAndWait();
                return false;
            }

            for (var good : list) {
                if (good.goodId() == Integer.parseInt(goodField.getText()) &
                        Objects.equals(good.unitId(), unitField.getText())) {
                    errorAlert.setContentText("The input code already exists, update it instead");
                    errorAlert.showAndWait();
                    return false;
                }
            }
            return true;
        } else {
            errorAlert.setContentText("The input dates must be filled");
            errorAlert.showAndWait();
            return false;
        }
    }

    private boolean checkUnitExistence() {
        final var retUnit = operatingUnitTables.findByCode(toUpperNormalizer(unitField));
        return CommonCheckers.fieldChecker(List.of(retUnit));
    }

    public  void setFields() {
        if (vehicleCheck.isSelected()) {
            expirationPicker.setDisable(false);
            typeChoice.setDisable(false);
            plateField.setDisable(false);
            toolField.setDisable(true);
        } else {
            expirationPicker.setDisable(true);
            typeChoice.setDisable(true);
            plateField.setDisable(true);
            toolField.setDisable(false);
        }
    }

    private boolean plateNotAlreadyExists() {
        var list = capitalGoodsTables.findAll();
        List<Optional<String>> idList = list.stream().map(CapitalGood::licencePlate).toList();
        return utilities.checkers.CommonCheckers.isOptionalAlreadyPresent(idList, plateField);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expirationPicker.setDisable(true);
        typeChoice.setDisable(true);
        plateField.setDisable(true);
        typeChoice.getItems().addAll("BENZINA",
                "DIESEL",
                "METANO",
                "GPL",
                "ELETTRICA");
        typeChoice.setValue("BENZINA");
    }
}
