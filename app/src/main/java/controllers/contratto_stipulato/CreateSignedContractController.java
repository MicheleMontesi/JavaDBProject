package controllers.contratto_stipulato;

import db.ConnectionProvider;
import db.Table;
import db.tables.ContractTypeTables;
import db.tables.SignedContractsTables;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.SignedContract;
import static utilities.checkers.CommonCheckers.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class CreateSignedContractController {

    @FXML
    private TextField idField, nameField;
    @FXML
    private DatePicker signedPicker, endPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final SignedContractsTables scTables = new SignedContractsTables(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());
    private final ContractTypeTables ctTable = new ContractTypeTables(connectionProvider.getMySQLConnection());

    private String id;
    private Date signedDate;
    private Date endDate;
    private String name;

    public void create() {
        if (check() & caCheck(scTables, idField, nameField, signedPicker, endPicker)) {
            this.init();
            scTables.save(new SignedContract(id, signedDate, endDate, name));
        }
    }

    public void update() {
        if (check()) {
            this.init();
            scTables.update(new SignedContract(id, signedDate, endDate, name));
        }
    }

    private boolean check() {
        return lengthChecker(idField, 16, 16) &
                lengthChecker(nameField, 2, 30) &
                checkOpUnitExistence(workersTables, idField, ctTable, nameField);
    }

    private void init() {
        id = toUpperNormalizer(idField);
        signedDate = Date.from(Instant.from(signedPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        endDate = Date.from(Instant.from(endPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        name = toUpperNormalizer(nameField);
    }

    private boolean caCheck(Table<SignedContract, String> table, TextField idField, TextField nameField, DatePicker signedPicker, DatePicker endPicker) {
        var list = table.findAll();
        var signedDate = Date.from(Instant.from(signedPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        var endDate = Date.from(Instant.from(endPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        for (var cs : list) {
            if (cs.fiscalCode().equals(toUpperNormalizer(idField)) &&
                    cs.contractName().equals(toUpperNormalizer(nameField))) {
                if (getYearDifference(signedDate, cs.endDate()) < 0 || getYearDifference(endDate, cs.stipulationDate()) > 0) {
                    errorAlert.setContentText("The input date must be bigger than the end of the last contract with the same name");
                    errorAlert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }
}
