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
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.FillUtils.getList;
import static utilities.checkers.CommonCheckers.choiceBoxChecker;
import static utilities.checkers.PersonCheckers.toUpperNormalizer;

public class ShowSignedContractByCodeController implements Initializable {
    @FXML
    private ChoiceBox<String> idBox, nameBox;
    @FXML
    private TableView<SignedContract> table;
    @FXML
    private TableColumn<SignedContract, String> fiscalCodeColumn, nameColumn;
    @FXML
    private TableColumn<SignedContract, Date> stipulationColumn, endColumn;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final SignedContractsTables scTables = new SignedContractsTables(connectionProvider.getMySQLConnection());

    public void search() {
        if (
                choiceBoxChecker(idBox) &&
                choiceBoxChecker(nameBox)

        ) {
            var sc = scTables.findByParameters(idBox.getValue(), nameBox.getValue());
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
        if (idBox != null) {
            idBox.getItems().addAll(scTables.findAll().stream().map(SignedContract::fiscalCode).distinct().toList());
        }
        if (nameBox != null) {
            nameBox.getItems().addAll(scTables.findAll().stream().map(SignedContract::contractName).distinct().toList());
        }
        getList(idBox, scTables, e -> e.getId().get(0));
        getList(nameBox, scTables, e -> e.getId().get(2));
    }
}
