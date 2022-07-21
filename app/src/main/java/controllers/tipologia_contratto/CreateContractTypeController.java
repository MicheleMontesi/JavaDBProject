package controllers.tipologia_contratto;

import utilities.ConnectionProvider;
import db.Table;
import db.tables.ContractTypeTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.ContractType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import static utilities.checkers.PersonCheckers.*;

public class CreateContractTypeController implements Initializable {

    @FXML
    private TextField nameField, hoursField;
    @FXML
    private ChoiceBox<String> nameChoiceBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final ContractTypeTables ctTables = new ContractTypeTables(connectionProvider.getMySQLConnection());

    private String name;
    private int hours;

    public void create() {
        if (
                lengthChecker(nameField, 2, 20) &
                ctIsNotAlreadyPresent(nameField, ctTables, ContractType::name) &
                intCheck(hoursField, 1, 5)
        ) {
            name = toUpperNormalizer(nameField);
            hours = Integer.parseInt(hoursField.getText());

            ctTables.save(new ContractType(name, hours));
        }
    }

    public void update() {
        if (
                !nameChoiceBox.getSelectionModel().isEmpty() &
                intCheck(hoursField, 1, 4)
        ) {
            name = nameChoiceBox.getValue();
            hours = Integer.parseInt(hoursField.getText());

            ctTables.update(new ContractType(name, hours));
        }
    }

    public void fillFields() {
        if (!nameChoiceBox.getSelectionModel().isEmpty()) {
            var selected = nameChoiceBox.getSelectionModel().getSelectedItem();
            var ctList = ctTables.findByCode(selected);
            if (ctList.isPresent()) {
                var ct = ctList.get().stream().findFirst().isPresent() ? ctList.get().stream().findFirst().get() : null;
                if (ct != null) {
                    hoursField.setText(String.valueOf(ct.contractualHours()));
                }
            }
        }
    }

    private boolean ctIsNotAlreadyPresent(TextField field, Table<ContractType, String> table, Function<ContractType, ?> matchString) {
        var list = table.findAll();
        List<?> idList = list.stream().map(matchString).toList();
        return utilities.checkers.CommonCheckers.isAlreadyPresent(idList, field);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (nameChoiceBox != null) {
            nameChoiceBox.getItems().addAll(ctTables.findAll().stream().map(ContractType::name).toList());
        }
    }
}
