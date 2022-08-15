package controllers.terapia;

import utilities.ConnectionProvider;
import db.tables.TherapiesTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Therapy;
import utilities.views.CreateTherapyView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class TherapyViewController implements Initializable {

    @FXML
    private TableView<Therapy> table;
    @FXML
    private TableColumn<Therapy, Date> dateColumn;
    @FXML
    private TableColumn<Therapy, Integer> therapyColumn;
    @FXML
    private TableColumn<Therapy, String> descriptionColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());

    private final ObservableList<Therapy> list = FXCollections.observableArrayList(therapiesTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateTherapyView.create(table, therapyColumn, dateColumn, descriptionColumn, list);
    }
}
