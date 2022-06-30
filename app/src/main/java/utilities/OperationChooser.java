package utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.Normalizer;

public class OperationChooser {

    private String currentEntitySelection;
    private String currentOperationSelection;
    private BorderPane contentPane;

    public void choose() {
        try {
            switch (currentEntitySelection) {
                case "Dipendente":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateWorker");
                        case "Elimina" -> this.loadContent("DeleteWorker");
                        case "Visualizza" -> this.loadContent("View");
                        case "Ricerca Tramite Parametro" -> this.loadContent("WorkerSearchByFiscalCode");
                        case "Visualizza Turni" -> this.loadContent("WorkerShowShifts");
                        default -> this.contentPane.getChildren().clear();
                    }
                    break;
                case "Turno":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateShift");
                    }
                    break;
                case "Unita' Operativa":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateOpUnit");
                    }
                    break;
                case "Tipologia Attestato":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateCertificateType");
                    }
                    break;
                case "Attestato Acquisito":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateCertificateAcquired");
                    }
                    break;
                default:
                    this.contentPane.getChildren().clear();
                    break;
            }

        } catch (NullPointerException e) {
            System.out.println("Nothing to show here");
        }
    }

    private void loadContent(String file) {
        Parent root = null;
        var current = Normalizer.normalize(currentEntitySelection, Normalizer.Form.NFD)
                .toLowerCase()
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("'", "")
                .replaceAll("\\s+", "_");
        System.out.println(current);
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
