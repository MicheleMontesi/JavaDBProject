package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import utilities.CreateOperationsMap;
import utilities.OperationChooser;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private ListView<String> myEntityList;
    @FXML
    private ListView<String> myOperationList;
    @FXML
    private BorderPane contentPane;

    private final Map<String, List<String>> permittedOp = CreateOperationsMap.initOperationMap();
    private String currentEntitySelection;
    private String currentOperationSelection;
    private final OperationChooser chooser = new OperationChooser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myEntityList.getItems().addAll(permittedOp.keySet());
        chooser.setContentPane(contentPane);

        myEntityList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentEntitySelection = myEntityList.getSelectionModel().getSelectedItem();
            chooser.setCurrentEntitySelection(this.currentEntitySelection);
            System.out.println(currentEntitySelection);
            myOperationList.getItems().clear();
            myOperationList.getItems().addAll(permittedOp.get(currentEntitySelection));
            });

        myOperationList.getSelectionModel().selectedItemProperty().addListener((observable1, oldValue1, newValue1) -> {
            currentOperationSelection = myOperationList.getSelectionModel().getSelectedItem();
            chooser.setCurrentOperationSelection(this.currentOperationSelection);
            chooser.choose();
        });
    }
}
