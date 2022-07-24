package controllers.tipologia_attestato;

import db.tables.CertificateTypeTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CertificateType;
import utilities.ConnectionProvider;
import utilities.views.CreateCertificateTypeView;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class CertificateTypeSearchCodeController implements Initializable {

    @FXML
    private ChoiceBox<String> nameChoiceBox;
    @FXML
    private TableView<CertificateType> table;
    @FXML
    private TableColumn<CertificateType, String> nameColumn;
    @FXML
    private TableColumn<CertificateType, Integer> ecmColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final CertificateTypeTables ctTable = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(nameChoiceBox)) {
            var certificate = ctTable.findByCode(nameChoiceBox.getValue());
            if (certificate.isPresent()) {
                final ObservableList<CertificateType> list = FXCollections.observableArrayList(certificate.get());
                CreateCertificateTypeView.create(table, nameColumn, ecmColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + nameChoiceBox.getValue() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(nameChoiceBox, ctTable, e -> e.getId().get(0));
    }
}
