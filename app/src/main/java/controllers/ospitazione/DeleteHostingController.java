package controllers.ospitazione;

import db.tables.HostingTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import utilities.ConnectionProvider;
import utilities.FillUtils;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;
import static utilities.checkers.CommonCheckers.dateCheck;

public class DeleteHostingController implements Initializable {

    @FXML
    private ChoiceBox<String> fiscalCodeBox, unitIdBox;
    @FXML
    private DatePicker datePicker;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final HostingTables hostingTables = new HostingTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                choiceBoxChecker(fiscalCodeBox) &&
                choiceBoxChecker(unitIdBox) &&
                dateCheck(datePicker)
        ) {
            final var fiscalCode = fiscalCodeBox.getValue();
            final var date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
            final var unitId = unitIdBox.getValue();

            hostingTables.deleteByParameters(fiscalCode, date, unitId);
        }
    }

    public void fillRelatedField() {
        FillUtils.fillRelatedField(fiscalCodeBox, unitIdBox, hostingTables, 0, 2);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(fiscalCodeBox, hostingTables, e -> e.getId().get(0));
    }
}
