package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private ListView<String> myEntityList;
    @FXML
    private ListView<String> myOperationList;
    private List<String> defaultOperations = new ArrayList<>(Arrays.asList("Crea", "Elimina", "Visualizza"));
    private Map<String, List<String>> operationsMap= new HashMap<>(
            Map.of("Dipendente", new ArrayList<>(defaultOperations),
                    "Paziente", new ArrayList<>(defaultOperations),
                    "Unita' Operativa", new ArrayList<>(defaultOperations),
                    "Farmaco", new ArrayList<>(defaultOperations))
    );
    private String currentEntitySelection;

    private void initOperationMap() {
        operationsMap.get("Dipendente").addAll(Arrays.asList("Visualizza Turni dato un Nome", "esempio"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initOperationMap();
        myEntityList.getItems().addAll(operationsMap.keySet());
        myEntityList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentEntitySelection = myEntityList.getSelectionModel().getSelectedItem();
                System.out.println(currentEntitySelection);
                myOperationList.getItems().clear();
                myOperationList.getItems().addAll(operationsMap.get(currentEntitySelection));
            }
        });
    }
}
