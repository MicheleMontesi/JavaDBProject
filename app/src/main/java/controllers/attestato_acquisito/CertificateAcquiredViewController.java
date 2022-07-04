package controllers.attestato_acquisito;

import db.ConnectionProvider;
import db.tables.CertificateAcquiredTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CertificateAcquired;
import utilities.views.CreateCertificateAcquiredView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class CertificateAcquiredViewController implements Initializable {

    @FXML
    private TableView<CertificateAcquired> table;
    @FXML
    private TableColumn<CertificateAcquired, String> fiscalCodeColumn, nameColumn;
    @FXML
    private TableColumn<CertificateAcquired, Date> dateColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final CertificateAcquiredTables caTable = new CertificateAcquiredTables(connectionProvider.getMySQLConnection());

    private final ObservableList<CertificateAcquired> list = FXCollections.observableArrayList(caTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateCertificateAcquiredView.create(table, fiscalCodeColumn, nameColumn, dateColumn, list);
    }
}
