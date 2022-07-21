package controllers.assumere_terapia;

import db.tables.TakeTherapiesTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.TakeTherapy;
import utilities.ConnectionProvider;
import utilities.views.CreateTakeTherapyView;

import java.net.URL;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class TakeTherapySearchCodeController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;
    @FXML
    private TableView<TakeTherapy> table;
    @FXML
    private TableColumn<TakeTherapy, String> fiscalCodeColumn;
    @FXML
    private TableColumn<TakeTherapy, Integer> therapyColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final TakeTherapiesTables ttTable = new TakeTherapiesTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(idBox)) {
            var takeTherapy = ttTable.findByCode(idBox.getValue());
            final ObservableList<TakeTherapy> list = FXCollections.observableArrayList(takeTherapy.orElse(null));
            CreateTakeTherapyView.create(table, fiscalCodeColumn, therapyColumn, list);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (idBox != null) {
            idBox.getItems().addAll(ttTable.findAll().stream().map(TakeTherapy::fiscalCode).distinct().toList());
        }
    }
}
