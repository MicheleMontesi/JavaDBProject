package controllers.farmaco_terapia;

import db.Table;
import db.tables.DrugsTables;
import db.tables.TherapiesTable;
import db.tables.TherapyDrugsTable;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.*;
import utilities.ConnectionProvider;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;
import static utilities.checkers.CommonCheckers.dateCheck;
import static utilities.checkers.PersonCheckers.intCheck;

public class CreateTherapyDrugsController implements Initializable {
    @FXML
    private TextField consumptionField, quantityField;
    @FXML
    private ChoiceBox<String> therapyIdBox, fiscalCodeBox, drugIdBox, consumptionIdBox;
    @FXML
    private DatePicker datePicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final TherapyDrugsTable tdTable = new TherapyDrugsTable(connectionProvider.getMySQLConnection());
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());
    private final DrugsTables drugsTables = new DrugsTables(connectionProvider.getMySQLConnection());

    private int therapyId, consumptionId, quantity, drugId;
    private Date date;
    private String fiscalCode;

    public void create() {
        if (
                check() &&
                intCheck(consumptionField, 1, 6)
        ) {
            this.init();
            if (checkDrugAvailability(Objects.toString(drugId), quantity)) {
                consumptionId = Integer.parseInt(consumptionField.getText());
                sumDrugs(Objects.toString(drugId), quantity, true);
                tdTable.save(new TherapyDrug(therapyId, consumptionId, date, quantity, fiscalCode, drugId));
            }
        }
    }

    public void update() {
        if (check()) {
            this.init();
            consumptionId = Integer.parseInt(consumptionIdBox.getValue());
            var qt = Objects.requireNonNull(tdTable.findByParameters(therapyId, consumptionId).orElse(null)).get(0).quantity();
            sumDrugs(Objects.toString(drugId), qt, false);
            if (checkDrugAvailability(Objects.toString(drugId), quantity)) {
                sumDrugs(Objects.toString(drugId), quantity, true);
                tdTable.update(new TherapyDrug(therapyId, consumptionId, date, quantity, fiscalCode, drugId));
            } else {
                sumDrugs(Objects.toString(drugId), qt, false);
            }
        }
    }

    private boolean check() {
        return choiceBoxChecker(therapyIdBox) &&
                dateCheck(datePicker) &&
                intCheck(quantityField, 1, 6) &&
                choiceBoxChecker(fiscalCodeBox) &&
                choiceBoxChecker(drugIdBox);
    }

    private void init() {
        therapyId = Integer.parseInt(therapyIdBox.getValue());
        date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        quantity = Integer.parseInt(quantityField.getText());
        fiscalCode = fiscalCodeBox.getValue();
        drugId = Integer.parseInt(drugIdBox.getValue());
    }

    private Drug findDrug(String id) {
        return drugsTables.findByCode(id).isPresent() ?
                drugsTables.findByCode(id).get().stream().findFirst().isPresent() ?
                        drugsTables.findByCode(id).get().stream().findFirst().get() : null
                : null;
    }

    private void sumDrugs(String id, int quantity, boolean negative) {
        var drug = findDrug(id);
        if (drug != null) {
            var qt = negative ? drug.quantity() - quantity : drug.quantity() + quantity;
            var newDrug = new Drug(drug.drugId(), drug.name(), drug.pharmaCompany(), drug.purchaseDate(), drug.expirationDate(), qt);
            drugsTables.update(newDrug);
        }
    }

    private boolean checkDrugAvailability(String id, int quantity) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("There are not enough drugs");
        var drug = findDrug(id);
        if (drug != null) {
            var qt = drug.quantity() - quantity;
            if (qt < 0) {
                errorAlert.showAndWait();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (consumptionField != null) {
            var max = tdTable.findAll()
                    .stream()
                    .map(TherapyDrug::consumptionId)
                    .toList()
                    .stream()
                    .mapToInt(v -> v)
                    .max()
                    .orElse(0);
            consumptionField.setText(Integer.toString(max + 1));
        }

        getList(fiscalCodeBox, workersTables, e -> e.getId().get(0));
        getList(therapyIdBox, therapiesTable, e -> e.getId().get(0));
        getList(drugIdBox, drugsTables, e -> e.getId().get(0));
        getList(consumptionIdBox, tdTable, e -> e.getId().get(1));
    }
}
