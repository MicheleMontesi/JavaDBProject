package controllers.assumere_terapia;

import db.ConnectionProvider;
import db.tables.TakeTherapiesTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.TakeTherapy;
import model.Worker;
import utilities.views.CreateTakeTherapyView;

import java.net.URL;
import java.util.ResourceBundle;

public class TakeTherapyViewController implements Initializable {

    @FXML
    private TableView<TakeTherapy> table;
    @FXML
    private TableColumn<TakeTherapy, String> fiscalCodeColumn;
    @FXML
    private TableColumn<TakeTherapy, Integer> therapyColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final TakeTherapiesTables ttTable = new TakeTherapiesTables(connectionProvider.getMySQLConnection());

    private final ObservableList<TakeTherapy> list = FXCollections.observableArrayList(ttTable.findAll());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateTakeTherapyView.create(table, fiscalCodeColumn, therapyColumn, list);
    }
}
