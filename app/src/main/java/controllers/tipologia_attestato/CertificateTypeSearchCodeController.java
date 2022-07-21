package controllers.tipologia_attestato;

import utilities.ConnectionProvider;
import db.tables.CertificateTypeTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.CertificateType;
import utilities.FXUtils;
import utilities.views.CreateCertificateTypeView;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class CertificateTypeSearchCodeController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<CertificateType> table;
    @FXML
    private TableColumn<CertificateType, String> nameColumn;
    @FXML
    private TableColumn<CertificateType, Integer> ecmColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final CertificateTypeTables ctTable = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(nameField, 1, 30)) {
            var certificate = ctTable.findByCode(toUpperNormalizer(nameField));
            if (certificate.isPresent()) {
                final ObservableList<CertificateType> list = FXCollections.observableArrayList(certificate.get());
                CreateCertificateTypeView.create(table, nameColumn, ecmColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + nameField.getText() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXUtils.disableOnWrite(searchButton, nameField);
    }
}
