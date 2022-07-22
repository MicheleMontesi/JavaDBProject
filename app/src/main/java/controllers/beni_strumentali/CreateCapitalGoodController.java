package controllers.beni_strumentali;

import db.tables.CapitalGoodsTables;
import db.tables.OperatingUnitTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.CapitalGood;
import utilities.ConnectionProvider;
import utilities.FillUtils;
import utilities.checkers.CommonCheckers;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;
import static utilities.checkers.CommonCheckers.getYearDifference;
import static utilities.checkers.PersonCheckers.*;

public class CreateCapitalGoodController implements Initializable {

    @FXML
    private ChoiceBox<String> unitBox, goodBox;
    @FXML
    public TextField goodField, toolField, plateField;
    @FXML
    public DatePicker purchasePicker, maintenancePicker, expirationPicker;
    @FXML
    public CheckBox vehicleCheck;
    @FXML
    public ChoiceBox<String> typeChoice;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final CapitalGoodsTables capitalGoodsTables = new CapitalGoodsTables(connectionProvider.getMySQLConnection());
    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());

    private String unitId;
    private int goodId;
    private Date purchaseDate;
    private Date maintenanceDate;
    private boolean vehicle;
    private Optional<String> plate;
    private Optional<String> type;
    private Optional<Date> expirationDate;
    private Optional<String> toolName;

    public void create() {
        if (
            check() &&
            intCheck(goodField, 1, 5) &&
            alreadyExists(Integer.parseInt(goodField.getText()))
        ) {
            this.init();
            this.vehicleOrToolCheck();
            goodId = Integer.parseInt(goodField.getText());
            capitalGoodsTables.save(new CapitalGood(unitId, goodId, purchaseDate, maintenanceDate, vehicle,
                    toolName, plate, type, expirationDate));
        }
    }

    public void update() {
        if (check()) {
            this.init();
            this.vehicleOrToolCheck();
            goodId = Integer.parseInt(goodBox.getValue());
            capitalGoodsTables.update(new CapitalGood(unitId, goodId, purchaseDate, maintenanceDate, vehicle,
                    toolName, plate, type, expirationDate));
        }
    }

    private boolean check() {
        return choiceBoxChecker(unitBox) &&
        checkUnitExistence() &&
        dateCheck();
    }

    private void init() {
        unitId = unitBox.getValue();
        purchaseDate = Date.from(Instant.from(purchasePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        maintenanceDate = Date.from(Instant.from(maintenancePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        vehicle = vehicleCheck.isSelected();
        plate = Optional.empty();
        type = Optional.empty();
        expirationDate = Optional.empty();
        toolName = Optional.empty();
    }

    private void vehicleOrToolCheck() {
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
            }
        } else {
            if (lengthChecker(toolField, 2, 20)) {
                toolName = Optional.of(toUpperNormalizer(toolField));
            }
        }
    }

    private boolean dateCheck() {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        if (purchasePicker.getValue() != null || maintenancePicker != null) {
            final var purchaseDate = Date.from(Instant.from(purchasePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final var maintenanceDate = Date.from(Instant.from(maintenancePicker.getValue().atStartOfDay(ZoneId.systemDefault())));

            if (getYearDifference(purchaseDate, maintenanceDate) < 0) {
                errorAlert.setContentText("The input maintenance date must be one year bigger than the purchase date");
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

    private boolean alreadyExists(int goodId) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        final var list = capitalGoodsTables.findAll();
        for (var good : list) {
            if (good.goodId() == goodId &
                    Objects.equals(good.unitId(), unitBox.getValue())) {
                errorAlert.setContentText("The input code already exists, update it instead");
                errorAlert.showAndWait();
                return false;
            }
        }
        return true;
    }

    private boolean checkUnitExistence() {
        final var retUnit = operatingUnitTables.findByCode(unitBox.getValue());
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

    public void fillGoodField() {
        FillUtils.fillGoodField(unitBox, goodBox, capitalGoodsTables);
    }

    public void fillFields() {
        if (!goodBox.getSelectionModel().isEmpty()) {
            var selectedUnit = unitBox.getSelectionModel().getSelectedItem();
            var selectedGood = Integer.parseInt(goodBox.getSelectionModel().getSelectedItem());
            var goodList = capitalGoodsTables.findByParameters(selectedUnit, selectedGood);
            if (goodList.isPresent()) {
                var good = goodList.get().stream().findFirst().orElse(null);
                if (good != null) {
                    toolField.setText(good.toolName().orElse(null));
                    plateField.setText(good.licencePlate().orElse(null));
                    purchasePicker.getEditor().setText(good.purchaseDate().toString());
                    maintenancePicker.getEditor().setText(good.nextMaintenance().toString());
                    expirationPicker.getEditor().setText(good.insuranceExpiration().isPresent() ? good.insuranceExpiration().get().toString() : null);
                    vehicleCheck.setSelected(good.vehicle());
                    typeChoice.setValue(good.typology().orElse(null));
                }
            }
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
        if (typeChoice != null) {
            typeChoice.getItems().addAll("BENZINA",
                    "DIESEL",
                    "METANO",
                    "GPL",
                    "ELETTRICA");
            typeChoice.setValue("BENZINA");
        }
        if (unitBox != null) {
            unitBox.getItems().addAll(capitalGoodsTables.findAll().stream().map(CapitalGood::unitId).distinct().toList());
        }
    }
}
