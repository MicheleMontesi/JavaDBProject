package controllers.assumere_terapia;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import model.Patient;
import model.Therapy;
import model.Worker;
import utilities.ConnectionProvider;
import db.tables.PatientsTables;
import db.tables.TakeTherapiesTables;
import db.tables.TherapiesTable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.TakeTherapy;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.*;

public class CreateTakeTherapyController implements Initializable {
    @FXML
    private ChoiceBox<String> fiscalCodeBox, therapyIdBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final TakeTherapiesTables ttTable = new TakeTherapiesTables(connectionProvider.getMySQLConnection());
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());
    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (check()) {
            final var fiscalCode = fiscalCodeBox.getValue();
            final var therapyId = Integer.parseInt(therapyIdBox.getValue());

            ttTable.save(new TakeTherapy(fiscalCode, therapyId));
        }
    }

    public void update() {
        if (check()) {
            final var fiscalCode = fiscalCodeBox.getValue();
            final var therapyId = Integer.parseInt(therapyIdBox.getValue());

            ttTable.update(new TakeTherapy(fiscalCode, therapyId));
        }
    }

    private boolean check() {
        return choiceBoxChecker(fiscalCodeBox) &
                choiceBoxChecker(therapyIdBox);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (fiscalCodeBox != null) {
            fiscalCodeBox.getItems().addAll(patientsTables.findAll().stream().map(Patient::fiscalCode).toList());
        }
        if (therapyIdBox != null) {
            therapyIdBox.getItems().addAll(therapiesTable.findAll().stream().map(Therapy::therapyId).map(Objects::toString).toList());
        }
    }
}
