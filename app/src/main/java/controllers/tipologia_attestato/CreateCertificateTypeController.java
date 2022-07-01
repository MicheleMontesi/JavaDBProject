package controllers.tipologia_attestato;

import db.ConnectionProvider;
import db.Table;
import db.tables.CertificateTypeTables;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.CertificateType;

import java.util.List;
import java.util.function.Function;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class CreateCertificateTypeController {

    @FXML
    private TextField nameField;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final CertificateTypeTables ctTables = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    public void create() {
        if (
                lengthChecker(nameField, 2, 30) &
                ctIsNotAlreadyPresent(nameField, ctTables, CertificateType::name)
        ) {
            final String name = toUpperNormalizer(nameField);

            ctTables.save(new CertificateType(name));
        }
    }

    private boolean ctIsNotAlreadyPresent(TextField field, Table<CertificateType, String> table, Function<CertificateType, ?> matchString) {
        var list = table.findAll();
        List<?> idList = list.stream().map(matchString).toList();
        return utilities.checkers.CommonCheckers.isAlreadyPresent(idList, field);
    }
}