package controllers.attestato_acquisito;

import db.tables.CertificateAcquiredTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CertificateAcquired;
import utilities.ConnectionProvider;
import utilities.views.CreateCertificateAcquiredView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class ShowCertificatesByCodeController implements Initializable {
    @FXML
    private ChoiceBox<String> idBox;
    @FXML
    private TableView<CertificateAcquired> table;
    @FXML
    private TableColumn<CertificateAcquired, String> fiscalCodeColumn, nameColumn;
    @FXML
    private TableColumn<CertificateAcquired, Date> dateColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final CertificateAcquiredTables caTables = new CertificateAcquiredTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(idBox)) {
            var certificate = caTables.findByCode(idBox.getValue());
            if (certificate.isPresent()) {
                final ObservableList<CertificateAcquired> list = FXCollections.observableArrayList(certificate.get());
                CreateCertificateAcquiredView.create(table, fiscalCodeColumn, nameColumn, dateColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The fiscal code \"" + idBox.getValue() + "\" doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(idBox, caTables, e -> e.getId().get(0));
    }
}
