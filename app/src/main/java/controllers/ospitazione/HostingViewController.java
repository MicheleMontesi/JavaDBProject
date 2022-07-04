package controllers.ospitazione;

import db.ConnectionProvider;
import db.tables.HostingTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Hosting;
import utilities.views.CreateHostingView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class HostingViewController implements Initializable {
    @FXML
    private TableView<Hosting> table;
    @FXML
    private TableColumn<Hosting, String> fiscalCodeColumn, unitIdColumn;
    @FXML
    private TableColumn<Hosting, Date> endColumn, signedColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final HostingTables hostingTables = new HostingTables(connectionProvider.getMySQLConnection());

    private final ObservableList<Hosting> list = FXCollections.observableArrayList(hostingTables.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateHostingView.create(table, fiscalCodeColumn, signedColumn, endColumn, unitIdColumn, list);
    }
}
