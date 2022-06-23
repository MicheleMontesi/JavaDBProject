package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import utilities.CreateOperationsMap;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private ListView<String> myEntityList;
    @FXML
    private ListView<String> myOperationList;
    private final Map<String, List<String>> permittedOp = CreateOperationsMap.initOperationMap();
    private String currentEntitySelection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
/*        this.initOperationMap();*/
        myEntityList.getItems().addAll(permittedOp.keySet());
        myEntityList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentEntitySelection = myEntityList.getSelectionModel().getSelectedItem();
            System.out.println(currentEntitySelection);
            myOperationList.getItems().clear();
            myOperationList.getItems().addAll(permittedOp.get(currentEntitySelection));
        });
    }
}
