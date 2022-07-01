package controllers.tipologia_contratto;

import db.ConnectionProvider;
import db.Table;
import db.tables.ContractTypeTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.ContractType;

import java.util.List;
import java.util.function.Function;

import static utilities.checkers.PersonCheckers.*;

public class CreateContractTypeController {

    @FXML
    private TextField nameField, hoursField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final ContractTypeTables ctTables = new ContractTypeTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                lengthChecker(nameField, 2, 20) &
                ctIsNotAlreadyPresent(nameField, ctTables, ContractType::name) &
                intCheck(hoursField, 1, 5)
        ) {
            var name = toUpperNormalizer(nameField);
            var hours = Integer.parseInt(hoursField.getText());

            ctTables.save(new ContractType(name, hours));
        }
    }

    private boolean ctIsNotAlreadyPresent(TextField field, Table<ContractType, String> table, Function<ContractType, ?> matchString) {
        var list = table.findAll();
        List<?> idList = list.stream().map(matchString).toList();
        return utilities.checkers.CommonCheckers.isAlreadyPresent(idList, field);
    }
}
