package controllers.unita_operativa;

import utilities.ConnectionProvider;
import db.Table;
import db.tables.OperatingUnitTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.OperatingUnit;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import static utilities.checkers.PersonCheckers.*;

public class CreateOpUnitController implements Initializable {

    @FXML
    private TextField idField, nameField, locationField, bedsField, patientsField;
    @FXML
    private ChoiceBox<String> typeChoice, unitIdBox;
    @FXML
    private CheckBox authCheck, accreditCheck;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final OperatingUnitTables operatingUnitTables = new OperatingUnitTables(connectionProvider.getMySQLConnection());

    private String type;
    private String name;
    private String location;
    private int beds;
    private int patients;
    private boolean auth;
    private boolean accredit;

    public void create() {
        if (
                lengthChecker(idField, 1, 5) &
                unitIsNotAlreadyPresent(idField, operatingUnitTables, OperatingUnit::unitId) &
                check()
        ) {
            final String id = toUpperNormalizer(idField);
            this.init();
            operatingUnitTables.save(new OperatingUnit(id, type, name, location, beds, patients, auth, accredit));
        }
    }

    public void update() {
        if (check()) {
            final String id = unitIdBox.getValue();
            this.init();
            operatingUnitTables.update(new OperatingUnit(id, type, name, location, beds, patients, auth, accredit));
        }
    }

    private boolean check() {
        return !typeChoice.getSelectionModel().isEmpty() &
                lengthChecker(nameField, 2, 20) &
                lengthChecker(locationField, 5, 50) &
                intCheck(bedsField, 1, 5) &
                intCheck(patientsField, 1, 5) &
                authCheck.isSelected() &
                accreditCheck.isSelected();
    }

    private void init() {
        type = typeChoice.getValue();
        name = nameField.getText();
        location = locationField.getText();
        beds = Integer.parseInt(bedsField.getText());
        patients = Integer.parseInt(patientsField.getText());
        auth = authCheck.isSelected();
        accredit = accreditCheck.isSelected();
    }

    private boolean unitIsNotAlreadyPresent(TextField field, Table<OperatingUnit, String> table, Function<OperatingUnit, ?> matchString) {
        var list = table.findAll();
        List<?> idList = list.stream().map(matchString).toList();
        return utilities.checkers.CommonCheckers.isAlreadyPresent(idList, field);
    }

    public void fillFields() {
        if (!unitIdBox.getSelectionModel().isEmpty()) {
            var selected = unitIdBox.getSelectionModel().getSelectedItem();
            var ouList = operatingUnitTables.findByCode(selected);
            if (ouList.isPresent()) {
                var ou = ouList.get().stream().findFirst().isPresent() ? ouList.get().stream().findFirst().get() : null;
                if (ou != null) {
                    typeChoice.getSelectionModel().select(ou.type());
                    nameField.setText(ou.name());
                    locationField.setText(ou.location());
                    bedsField.setText(String.valueOf(ou.bedsNumber()));
                    patientsField.setText(String.valueOf(ou.patientsNumber()));
                    authCheck.setSelected(true);
                    accreditCheck.setSelected(true);
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeChoice.getItems().addAll("Gruppo Appartamento",
                "Residenza Sanitaria Psichiatrica");
        if (unitIdBox != null) {
            unitIdBox.getItems().addAll(operatingUnitTables.findAll().stream().map(OperatingUnit::unitId).toList());
        }
    }
}
