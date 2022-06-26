package utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.nio.file.Paths;

public class OperationChooser {

    private String currentEntitySelection;
    private String currentOperationSelection;
    private BorderPane contentPane;

    public void choose() {
        switch (currentEntitySelection) {
            case "Dipendente":
                switch (currentOperationSelection) {
                    case "Crea" -> this.loadContent("CreateWorker");
                    case "Elimina" -> this.loadContent("DeleteWorker");
                    case "Visualizza" -> this.loadContent("View");
                    case "Ricerca Tramite Parametro" -> this.loadContent("WorkerSearchByFiscalCode");
                    case "Visualizza Turni" -> this.loadContent("WorkerShowTurns");
                    default -> this.contentPane.getChildren().clear();
                }
                break;
            default:
                this.contentPane.getChildren().clear();
                break;
        }
    }

    private void loadContent(String file) {
        Parent root = null;
        try {
            root = FXMLLoader.load(ClassLoader.getSystemResource(String.valueOf(Paths.get("scenes",
                    currentEntitySelection.toLowerCase(), file + ".fxml"))));
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
