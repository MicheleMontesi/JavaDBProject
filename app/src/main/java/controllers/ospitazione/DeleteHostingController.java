package controllers.ospitazione;

import db.tables.HostingTables;
import db.tables.OperatingUnitTables;
import db.tables.PatientsTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import model.OperatingUnit;
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
    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());


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
            this.decreasePatients(unitId);
        }
    }

    private void decreasePatients(String unitId) {
        var opUnit = operatingUnitTables.findByCode(unitId);
        if (opUnit.isPresent()) {
            var patientsNumber = opUnit.get().get(0).patientsNumber();
            CreateHostingController.editPatientsNumber(false, patientsNumber, opUnit.get().get(0));
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
