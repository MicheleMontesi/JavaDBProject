package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import utilities.CreateOperationsMap;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private ListView<String> myEntityList;
    @FXML
    private ListView<String> myOperationList;
    @FXML
    private AnchorPane contentPane;
    private final Map<String, List<String>> permittedOp = CreateOperationsMap.initOperationMap();
    private String currentEntitySelection;
    private String currentOperationSelection;

    @FXML
    private void itemMembers() {
        Node node;
        try {
            node = FXMLLoader.load(ClassLoader.getSystemResource("Scenes/WorkerSearchByParameter.fxml"));
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myEntityList.getItems().addAll(permittedOp.keySet());

        myEntityList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentEntitySelection = myEntityList.getSelectionModel().getSelectedItem();
            System.out.println(currentEntitySelection);
            myOperationList.getItems().clear();
            myOperationList.getItems().addAll(permittedOp.get(currentEntitySelection));
            });

        myOperationList.getSelectionModel().selectedItemProperty().addListener((observable1, oldValue1, newValue1) -> {
            currentOperationSelection = myOperationList.getSelectionModel().getSelectedItem();
            if (currentOperationSelection != null) {
                System.out.println(currentOperationSelection);
                if (currentOperationSelection.equals("Ricerca Tramite Parametro")) {
                    itemMembers(); //non va cerca di correggere
                }

            }
        });
    }
}
