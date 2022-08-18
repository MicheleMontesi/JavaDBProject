package utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.Normalizer;

import static utilities.CreateOperationsMap.entitiesMap;

public class OperationChooser {

    private String currentEntitySelection;
    private String currentOperationSelection;
    private BorderPane contentPane;

    public void choose() {
        for (var choice : CreateOperationsMap.entities) {
            try {
                if (currentEntitySelection.equals(choice)) {
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("Create" + entitiesMap.get(currentEntitySelection));
                        case "Aggiorna" -> this.loadContent("Update" + entitiesMap.get(currentEntitySelection));
                        case "Elimina" -> this.loadContent("Delete" + entitiesMap.get(currentEntitySelection));
                        case "Visualizza" -> this.loadContent("View" + entitiesMap.get(currentEntitySelection));
                        case "Ricerca Tramite Codice" -> this.loadContent("Search" + entitiesMap.get(currentEntitySelection));
                        case "Ricerca Per Anno" -> this.loadContent("ViewPerYear" + entitiesMap.get(currentEntitySelection));
                        case "Ricerca Strutture Piene" -> this.loadContent("ViewFull" + entitiesMap.get(currentEntitySelection));
                        default -> this.contentPane.getChildren().clear();
                    }
                }
            } catch (NullPointerException e) {
                this.contentPane.getChildren().clear();
            }
        }
    }

    private void loadContent(String file) {
        Parent root = null;
        var current = Normalizer.normalize(currentEntitySelection, Normalizer.Form.NFD)
                .toLowerCase()
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("'", "")
                .replaceAll("\\s+", "_");
        try {
            root = FXMLLoader.load(ClassLoader.getSystemResource(String.valueOf(Paths.get("scenes",
                    current, file + ".fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentPane.setCenter(root);
    }

    public void setCurrentEntitySelection(String currentEntitySelection) {
        this.currentEntitySelection = currentEntitySelection;
    }

    public void setCurrentOperationSelection(String currentOperationSelection) {
        this.currentOperationSelection = currentOperationSelection;
    }

    public void setContentPane(BorderPane contentPane) {
        this.contentPane = contentPane;
    }
}
