package controllers.contratto_stipulato;

import utilities.ConnectionProvider;
import db.tables.SignedContractsTables;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.SignedContract;
import utilities.views.CreateSignedContractView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class ShowSignedContractByCodeController implements Initializable {
    @FXML
    private TextField fiscalCodeField, nameField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<SignedContract> table;
    @FXML
    private TableColumn<SignedContract, String> fiscalCodeColumn, nameColumn;
    @FXML
    private TableColumn<SignedContract, Date> stipulationColumn, endColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final SignedContractsTables scTables = new SignedContractsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (
                lengthChecker(fiscalCodeField, 16, 16) &
                lengthChecker(nameField, 2, 20)

        ) {
            var sc = scTables.findByParameters(toUpperNormalizer(fiscalCodeField),
                    toUpperNormalizer(nameField));
            if (sc.isPresent()) {
                final ObservableList<SignedContract> list = FXCollections.observableArrayList(sc.get());
                CreateSignedContractView.create(table, fiscalCodeColumn, stipulationColumn, endColumn, nameColumn, list);
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
                fiscalCodeField.getText().isEmpty() || nameField.getText().isEmpty(),
                fiscalCodeField.textProperty(), nameField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
