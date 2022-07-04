package controllers.attestato_acquisito;

import db.ConnectionProvider;
import db.tables.CertificateAcquiredTables;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.CertificateAcquired;
import utilities.views.CreateCertificateAcquiredView;

import static utilities.checkers.PersonCheckers.*;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ShowCertificatesByCodeController implements Initializable {
    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<CertificateAcquired> table;
    @FXML
    private TableColumn<CertificateAcquired, String> fiscalCodeColumn, nameColumn;
    @FXML
    private TableColumn<CertificateAcquired, Date> dateColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final CertificateAcquiredTables caTables = new CertificateAcquiredTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (lengthChecker(idField, 16, 16)) {
            var certificate = caTables.findByCode(toUpperNormalizer(idField));
            if (certificate.isPresent()) {
                final ObservableList<CertificateAcquired> list = FXCollections.observableArrayList(certificate.get());
                CreateCertificateAcquiredView.create(table, fiscalCodeColumn, nameColumn, dateColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idField.getText() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding idFieldValid = Bindings.createBooleanBinding(() -> idField.getText().isEmpty(), idField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
