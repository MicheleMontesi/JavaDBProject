package controllers.tipologia_attestato;

import db.ConnectionProvider;
import db.tables.CertificateTypeTables;
import db.tables.TherapiesTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CertificateType;
import model.Therapy;
import utilities.views.CreateCertificateTypeView;
import utilities.views.CreateTherapyView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class CertificateTypeViewController implements Initializable {

    @FXML
    private TableView<CertificateType> table;
    @FXML
    private TableColumn<CertificateType, String> nameColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final CertificateTypeTables ctTable = new CertificateTypeTables(connectionProvider.getMySQLConnection());

    private final ObservableList<CertificateType> list = FXCollections.observableArrayList(ctTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateCertificateTypeView.create(table, nameColumn, list);
    }
}