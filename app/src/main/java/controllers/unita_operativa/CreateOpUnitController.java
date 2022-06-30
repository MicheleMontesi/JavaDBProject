package controllers.unita_operativa;

import db.ConnectionProvider;
import db.Table;
import db.tables.OperatingUnitTables;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.OperatingUnit;

import java.util.List;
import java.util.function.Function;

import static utilities.checkers.PersonCheckers.*;

public class CreateOpUnitController {

    @FXML
    private TextField idField, nameField, locationField, bedsField, patientsField;
    @FXML
    private ChoiceBox<String> typeChoice;
    @FXML
    private CheckBox authCheck, accreditCheck;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                lengthChecker(idField, 1, 5) &
                unitIsNotAlreadyPresent(idField, operatingUnitTables, OperatingUnit::unitId) &
                !typeChoice.getSelectionModel().isEmpty() &
                lengthChecker(nameField, 2, 20) &
                lengthChecker(locationField, 5, 50) &
                intCheck(bedsField, 1, 5) &
                intCheck(patientsField, 1, 5) &
                authCheck.isSelected() &
                accreditCheck.isSelected()
        ) {
            final String id = toUpperNormalizer(idField);
            final String type = typeChoice.getValue();
            final String name = nameField.getText();
            final String location = locationField.getText();
            final int beds = Integer.parseInt(bedsField.getText());
            final int patients = Integer.parseInt(patientsField.getText());
            final boolean auth = authCheck.isSelected();
            final boolean accredit = accreditCheck.isSelected();

            operatingUnitTables.save(new OperatingUnit(id, type, name, location, beds, patients, auth, accredit));
        }
    }

    private boolean unitIsNotAlreadyPresent(TextField field, Table<OperatingUnit, String> table, Function<OperatingUnit, ?> matchString) {
        var list = table.findAll();
        List<?> idList = list.stream().map(matchString).toList();
        return utilities.checkers.CommonCheckers.isAlreadyPresent(idList, field);
    }

    public void fillTypology() {
        typeChoice.getItems().addAll("Gruppo Appartamento",
                "Residenza Sanitaria Psichiatrica");
    }
}
