package controllers.ospitazione;

import db.tables.HostingTables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Hosting;
import utilities.ConnectionProvider;
import utilities.FillUtils;
import utilities.checkers.CommonCheckers;
import utilities.views.CreateHostingView;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class ShowHostingByCodeController implements Initializable {
    @FXML
    private ChoiceBox<String> fiscalCodeBox, unitIdBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Hosting> table;
    @FXML
    private TableColumn<Hosting, String> fiscalCodeColumn, unitIdColumn;
    @FXML
    private TableColumn<Hosting, Date> endColumn, signedColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final HostingTables hostingTables = new HostingTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (
                choiceBoxChecker(fiscalCodeBox) &&
                choiceBoxChecker(unitIdBox) &&
                CommonCheckers.dateCheck(datePicker)

        ) {
            var hosting = hostingTables.findByParameters(fiscalCodeBox.getValue(),
                    Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()))),
                    unitIdBox.getValue());
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

    public void fillRelatedField() {
        FillUtils.fillRelatedField(fiscalCodeBox, unitIdBox, hostingTables, 0, 2);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getList(fiscalCodeBox, hostingTables, e -> e.getId().get(0));
    }
}
