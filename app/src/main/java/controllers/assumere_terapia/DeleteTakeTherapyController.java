package controllers.assumere_terapia;

import db.tables.TakeTherapiesTables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import model.TakeTherapy;
import utilities.ConnectionProvider;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static utilities.checkers.CommonCheckers.choiceBoxChecker;

public class DeleteTakeTherapyController implements Initializable {

    @FXML
    private ChoiceBox<String> fiscalCodeBox, therapyIdBox;

    private final ConnectionProvider connectionProvider = new ConnectionProvider();

    private final TakeTherapiesTables ttTable = new TakeTherapiesTables(connectionProvider.getMySQLConnection());

    public void delete() {
        if (
                choiceBoxChecker(fiscalCodeBox) &
                choiceBoxChecker(therapyIdBox)
        ) {
            final String id = fiscalCodeBox.getValue();
            final int therapyId = Integer.parseInt(therapyIdBox.getValue());
            ttTable.deleteByParameters(id, therapyId);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (fiscalCodeBox != null) {
            fiscalCodeBox.getItems().addAll(ttTable.findAll().stream().map(TakeTherapy::fiscalCode).distinct().toList());
        }
        if (therapyIdBox != null) {
            therapyIdBox.getItems().addAll(ttTable.findAll().stream().map(TakeTherapy::therapyId).map(Objects::toString).toList());
        }
    }
}
