package controllers.farmaco_terapia;

import db.ConnectionProvider;
import db.tables.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.TherapyDrug;
import model.Worker;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static utilities.checkers.PersonCheckers.*;

public class CreateTherapyDrugsController {
    @FXML
    public TextField therapyField, consumptionField, quantityField, fiscalCodeField, drugIdField;
    @FXML
    public DatePicker datePicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final TherapyDrugsTable tdTable = new TherapyDrugsTable(connectionProvider.getMySQLConnection());
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());
    private final DrugsTables drugsTables = new DrugsTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                intCheck(therapyField, 1, 10) &
                intCheck(consumptionField, 1, 6) &
                dateCheck(datePicker) &
                intCheck(quantityField, 1, 6) &
                lengthChecker(fiscalCodeField, 16, 16) &
                intCheck(drugIdField, 1, 10) &
                checkParametersExistence()
        ) {
            var therapyId = Integer.parseInt(therapyField.getText());
            var consumptionId = Integer.parseInt(consumptionField.getText());
            var date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            var quantity = Integer.parseInt(quantityField.getText());
            var fiscalCode = toUpperNormalizer(fiscalCodeField);
            var drugId = Integer.parseInt(drugIdField.getText());

            tdTable.save(new TherapyDrug(therapyId, consumptionId, date, quantity, fiscalCode, drugId));
        }
    }

    private boolean dateCheck(DatePicker datePicker) {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        if (datePicker.getValue() == null) {
            errorAlert.setContentText("The input dates must be filled");
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean checkParametersExistence() {
        final var retTherapy = therapiesTable.findByCode(toUpperNormalizer(therapyField));
        final var retWorker = workersTables.findByCode(toUpperNormalizer(fiscalCodeField));
        final var retDrug = drugsTables.findByCode(toUpperNormalizer(drugIdField));

        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The input TherapyId or FiscalCode or DrugId don't exist");

        if (retTherapy.isEmpty() || retWorker.isEmpty() || retDrug.isEmpty()) {
            errorAlert.showAndWait();
            return false;
        } else if (retTherapy.get().size() == 0 || retWorker.get().size() == 0 || retDrug.get().size() == 0) {
            errorAlert.showAndWait();
            return false;
        }
        return true;
    }

    public void setConsumptionField() {
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
}
