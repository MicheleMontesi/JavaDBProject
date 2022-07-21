package controllers.terapia;

import utilities.ConnectionProvider;
import db.tables.TherapiesTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Therapy;
import utilities.FXUtils;
import utilities.views.CreateTherapyView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.intCheck;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class TherapySearchCodeController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Therapy> table;
    @FXML
    private TableColumn<Therapy, Integer> therapyColumn;
    @FXML
    private TableColumn<Therapy, Date> dateColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final TherapiesTable therapiesTable = new TherapiesTable(connectionProvider.getMySQLConnection());

    public void search() {
        if (intCheck(idField, 1, 10)) {
            var therapy = therapiesTable.findByCode(toUpperNormalizer(idField));
            if (therapy.isPresent()) {
                final ObservableList<Therapy> list = FXCollections.observableArrayList(therapy.get());
                CreateTherapyView.create(table, therapyColumn, dateColumn, list);
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
        FXUtils.disableOnWrite(searchButton, idField);
    }
}
