package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ListView<String> myEntityList;
    @FXML
    private ListView<String> myOperationList;

    private List<String> entities = new ArrayList<>(Arrays.asList("Dipendente", "Paziente", "Unità Operativa", "Farmaco"));
    private String currentEntitySelection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myEntityList.getItems().addAll(entities);
    }
}
