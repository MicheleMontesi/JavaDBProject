package controllers.contratto_stipulato;

import db.tables.SignedContractsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import static utilities.checkers.CommonCheckers.choiceBoxChecker;
import static utilities.checkers.CommonCheckers.dateCheck;

public class DeleteSignedContractController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;
    @FXML
    private DatePicker datePicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final SignedContractsTables scTable = new SignedContractsTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                choiceBoxChecker(idBox) &&
                dateCheck(datePicker)
        ) {
            final var fiscalCode = idBox.getValue();
            final var date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));

            scTable.deleteByParameters(fiscalCode, date);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, scTable, e -> e.getId().get(0));
    }
}
