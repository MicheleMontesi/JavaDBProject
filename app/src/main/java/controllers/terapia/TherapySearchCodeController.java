package controllers.terapia;

import db.tables.TherapiesTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Therapy;
import utilities.ConnectionProvider;
import utilities.views.CreateTherapyView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class TherapySearchCodeController implements Initializable {

    @FXML
    private ChoiceBox<String> idBox;
    @FXML
    private TableView<Therapy> table;
    @FXML
    private TableColumn<Therapy, Integer> therapyColumn;
    @FXML
    private TableColumn<Therapy, Date> dateColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());

    public void search() {
        if (choiceBoxChecker(idBox)) {
            var therapy = therapiesTable.findByCode(idBox.getValue());
            if (therapy.isPresent()) {
                final ObservableList<Therapy> list = FXCollections.observableArrayList(therapy.get());
                CreateTherapyView.create(table, therapyColumn, dateColumn, list);
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
        getList(idBox, therapiesTable, e -> e.getId().get(0));
    }
}
