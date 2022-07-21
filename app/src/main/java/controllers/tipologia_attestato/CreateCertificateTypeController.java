package controllers.tipologia_attestato;

import utilities.ConnectionProvider;
import db.Table;
import db.tables.CertificateTypeTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.CertificateType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import static utilities.checkers.PersonCheckers.*;

public class CreateCertificateTypeController implements Initializable {

    @FXML
    private TextField nameField, ecmField;
    @FXML
    private ChoiceBox<String> nameChoiceBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final CertificateTypeTables ctTables = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    private String name;
    private int ecm;

    public void create() {
        if (
                lengthChecker(nameField, 2, 30) &
                intCheck(ecmField, 1, 4) &
                ctIsNotAlreadyPresent(nameField, ctTables, CertificateType::name)
        ) {
            name = toUpperNormalizer(nameField);
            ecm = Integer.parseInt(ecmField.getText());

            ctTables.save(new CertificateType(name, ecm));
        }
    }

    public void update() {
        if (
                !nameChoiceBox.getSelectionModel().isEmpty() &
                intCheck(ecmField, 1, 4)
        ) {
            name = nameChoiceBox.getValue();
            ecm = Integer.parseInt(ecmField.getText());

            ctTables.update(new CertificateType(name, ecm));
        }
    }

    private boolean ctIsNotAlreadyPresent(TextField field, Table<CertificateType, String> table, Function<CertificateType, ?> matchString) {
        var list = table.findAll();
        List<?> idList = list.stream().map(matchString).toList();
        return utilities.checkers.CommonCheckers.isAlreadyPresent(idList, field);
    }

    public void fillFields() {
        if (!nameChoiceBox.getSelectionModel().isEmpty()) {
            var selected = nameChoiceBox.getSelectionModel().getSelectedItem();
            var ctList = ctTables.findByCode(selected);
            if (ctList.isPresent()) {
                var ct = ctList.get().stream().findFirst().isPresent() ? ctList.get().stream().findFirst().get() : null;
                if (ct != null) {
                    ecmField.setText(String.valueOf(ct.ecmCredits()));
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (nameChoiceBox != null) {
            nameChoiceBox.getItems().addAll(ctTables.findAll().stream().map(CertificateType::name).toList());
        }
    }
}
