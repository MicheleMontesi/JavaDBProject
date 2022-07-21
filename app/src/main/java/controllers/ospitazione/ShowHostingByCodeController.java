package controllers.ospitazione;

import utilities.ConnectionProvider;
import db.tables.HostingTables;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Hosting;
import utilities.checkers.CommonCheckers;
import utilities.views.CreateHostingView;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.*;

public class ShowHostingByCodeController implements Initializable {
    @FXML
    private TextField fiscalCodeField, unitIdField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Hosting> table;
    @FXML
    private TableColumn<Hosting, String> fiscalCodeColumn, unitIdColumn;
    @FXML
    private TableColumn<Hosting, Date> endColumn, signedColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private final HostingTables hostingTables = new HostingTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (
                lengthChecker(fiscalCodeField, 16, 16) &
                CommonCheckers.dateCheck(datePicker)

        ) {
            var hosting = hostingTables.findByParameters(toUpperNormalizer(fiscalCodeField),
                    Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()))),
                    toUpperNormalizer(unitIdField));
            if (hosting.isPresent()) {
                final ObservableList<Hosting> list = FXCollections.observableArrayList(hosting.get());
                CreateHostingView.create(table, fiscalCodeColumn, signedColumn, endColumn, unitIdColumn, list);
            } else {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The input data doesn't exist.");
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding idFieldValid = Bindings.createBooleanBinding(() ->
                fiscalCodeField.getText().isEmpty() || datePicker.getEditor().getText().isEmpty() || unitIdField.getText().isEmpty(),
                fiscalCodeField.textProperty(), datePicker.getEditor().textProperty(), unitIdField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
