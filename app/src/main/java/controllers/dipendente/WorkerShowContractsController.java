package controllers.dipendente;

import db.ConnectionProvider;
import db.tables.SignedContractsTables;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.CertificateAcquired;
import model.SignedContract;
import org.checkerframework.checker.optional.qual.Present;
import utilities.views.CreateSignedContractView;

import java.util.Date;

import static utilities.checkers.PersonCheckers.lengthChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class WorkerShowContractsController {

    @FXML
    private TableView<SignedContract> table;
    @FXML
    private TableColumn<SignedContract, Date> beginColumn, endColumn;
    @FXML
    private TableColumn<SignedContract, String> nameColumn;
    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;

    private final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");
    private final SignedContractsTables scTables = new SignedContractsTables(connectionProvider.getMySQLConnection());

    public void search() {
        final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("The fiscal code \"" + idField.getText() + "\" doesn't exist.");

        if (lengthChecker(idField, 16, 16)) {
            var contract = scTables.findByCode(toUpperNormalizer(idField));
            if(contract.isPresent()) {
                if(contract.get().size() == 0) {
                    System.out.println(contract.get());
                    errorAlert.showAndWait();
                } else {
                    final ObservableList<SignedContract> list = FXCollections.observableArrayList(contract.get());
                    CreateSignedContractView.create(table, beginColumn, endColumn, nameColumn, list);
                }
            } else {
                System.out.println(contract.get());
                errorAlert.showAndWait();
            }
        }
    }

    public void disableOnWrite() {
        BooleanBinding idFieldValid = Bindings.createBooleanBinding(() -> idField.getText().isEmpty(), idField.textProperty());

        searchButton.disableProperty().bind(idFieldValid);
    }
}
