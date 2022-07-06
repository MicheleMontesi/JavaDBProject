package controllers.assumere_terapia;

import db.ConnectionProvider;
import db.tables.PatientsTables;
import db.tables.TakeTherapiesTables;
import db.tables.TherapiesTable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.TakeTherapy;
import utilities.checkers.CommonCheckers;

import java.util.List;

import static utilities.checkers.PersonCheckers.*;

public class CreateTakeTherapyController {
    @FXML
    private TextField fiscalCodeField, therapyIdField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final TakeTherapiesTables ttTable = new TakeTherapiesTables(connectionProvider.getMySQLConnection());
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());
    private final PatientsTables patientsTables = new PatientsTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (check()) {
            final var fiscalCode = toUpperNormalizer(fiscalCodeField);
            final var therapyId = Integer.parseInt(therapyIdField.getText());

            ttTable.save(new TakeTherapy(fiscalCode, therapyId));
        }
    }

    public void update() {
        if (check()) {
            final var fiscalCode = toUpperNormalizer(fiscalCodeField);
            final var therapyId = Integer.parseInt(therapyIdField.getText());

            ttTable.update(new TakeTherapy(fiscalCode, therapyId));
        }
    }

    private boolean check() {
        return lengthChecker(fiscalCodeField, 16, 16) &
                intCheck(therapyIdField, 1, 10) &
                checkParametersExistence();
    }

    private boolean checkParametersExistence() {
        final var retTherapy = therapiesTable.findByCode(toUpperNormalizer(therapyIdField));
        final var retPatient = patientsTables.findByCode(toUpperNormalizer(fiscalCodeField));
        return CommonCheckers.fieldChecker(List.of(retTherapy, retPatient));
    }
}
