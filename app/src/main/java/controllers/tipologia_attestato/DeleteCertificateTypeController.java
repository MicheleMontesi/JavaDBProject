package controllers.tipologia_attestato;

import db.tables.CertificateTypeTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteCertificateTypeController implements Initializable {

    @FXML
    private ChoiceBox<String> nameChoiceBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final CertificateTypeTables ctTable = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (choiceBoxChecker(nameChoiceBox)) {
            final String id = nameChoiceBox.getValue();

            ctTable.delete(id);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(nameChoiceBox, ctTable, e -> e.getId().get(0));
    }
}
