package controllers.contratto_stipulato;

import db.Table;
import db.tables.ContractTypeTables;
import db.tables.SignedContractsTables;
import db.tables.WorkersTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import model.SignedContract;
import utilities.ConnectionProvider;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.*;

public class CreateSignedContractController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox, nameBox;
    @FXML
    private DatePicker signedPicker, endPicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final SignedContractsTables scTables = new SignedContractsTables(connectionProvider.getMySQLConnection());
    private final WorkersTables workersTables = new WorkersTables(connectionProvider.getMySQLConnection());
    private final ContractTypeTables ctTable = new ContractTypeTables(connectionProvider.getMySQLConnection());

    private String id;
    private Date signedDate;
    private Date endDate;
    private String name;

    public void create() {
        if (check() & caCheck(scTables, idBox, nameBox, signedPicker, endPicker)) {
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
        return choiceBoxChecker(idBox) &&
                choiceBoxChecker(nameBox) &&
                checkExistence(workersTables, idBox.getValue(), ctTable, nameBox.getValue());
    }

    private void init() {
        id = idBox.getValue();
        signedDate = Date.from(Instant.from(signedPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        endDate = Date.from(Instant.from(endPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        name = nameBox.getValue();
    }

    private boolean caCheck(Table<SignedContract, String> table, ChoiceBox<String> idBox, ChoiceBox<String> nameBox,
                            DatePicker signedPicker, DatePicker endPicker) {
        var list = table.findAll();
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        if (signedPicker.getValue() == null || endPicker.getValue() == null) {
            errorAlert.setContentText("The date fields must be filled");
            errorAlert.showAndWait();
            return false;
        }
        var signedDate = Date.from(Instant.from(signedPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        var endDate = Date.from(Instant.from(endPicker.getValue().atStartOfDay(ZoneId.systemDefault())));

        for (var cs : list) {
            if (cs.fiscalCode().equals(idBox.getValue()) &&
                    cs.contractName().equals(nameBox.getValue())) {
                if (getYearDifference(signedDate, cs.endDate()) <= 0 || getYearDifference(endDate, cs.stipulationDate()) >= 0) {
                    errorAlert.setContentText("The input date must be bigger than the end of the last contract with the same name");
                    errorAlert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, workersTables, e -> e.getId().get(0));
        getList(nameBox, ctTable, e -> e.getId().get(0));
    }
}
