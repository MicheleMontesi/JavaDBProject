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
                        case "Aggiorna" -> this.loadContent("UpdateWorker");
                        case "Elimina" -> this.loadContent("DeleteWorker");
                        case "Visualizza" -> this.loadContent("WorkersView");
                        case "Ricerca Tramite Codice" -> this.loadContent("WorkerSearchByFiscalCode");
                        default -> this.contentPane.getChildren().clear();
                    }
                    break;
                case "Paziente":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreatePatient");
                        case "Aggiorna" -> this.loadContent("UpdatePatient");
                        case "Elimina" -> this.loadContent("DeletePatient");
                        case "Visualizza" -> this.loadContent("PatientsView");
                        case "Ricerca Tramite Codice" -> this.loadContent("PatientSearchByFiscalCode");
                    }
                    break;
                case "Turno":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateShift");
                        case "Elimina" -> this.loadContent("DeleteShift");
                        case "Visualizza" -> this.loadContent("ShiftsView");
                        case "Ricerca Tramite Codice" -> this.loadContent("ShiftSearchByCode");
                    }
                    break;
                case "Unita' Operativa":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateOpUnit");
                        case "Elimina" -> this.loadContent("DeleteOpUnit");
                        case "Visualizza" -> this.loadContent("OpUnitView");
                        case "Ricerca Tramite Codice" -> this.loadContent("OpUnitSearchByCode");
                    }
                    break;
                case "Tipologia Attestato":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateCertificateType");
                        case "Elimina" -> this.loadContent("DeleteCertificateType");
                        case "Visualizza" -> this.loadContent("CertificateTypeView");
                        case "Ricerca Tramite Codice" -> this.loadContent("CertificateTypeSearchCode");
                    }
                    break;
                case "Attestato Acquisito":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateCertificateAcquired");
                        case "Aggiorna" -> this.loadContent("UpdateCertificateAcquired");
                        case "Elimina" -> this.loadContent("DeleteCertificateAcquired");
                        case "Visualizza" -> this.loadContent("CertificateAcquiredView");
                        case "Ricerca Tramite Codice" -> this.loadContent("SearchCertificatesAcquiredByCode");
                    }
                    break;
                case "Tipologia Contratto":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateContractType");
                        case "Elimina" -> this.loadContent("DeleteContractType");
                        case "Visualizza" -> this.loadContent("ContractTypeView");
                        case "Ricerca Tramite Codice" -> this.loadContent("ContractTypeSearchCode");
                    }
                    break;
                case "Contratto Stipulato":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateSignedContract");
                        case "Aggiorna" -> this.loadContent("UpdateSignedContract");
                        case "Elimina" -> this.loadContent("DeleteSignedContract");
                        case "Visualizza" -> this.loadContent("SignedContractsView");
                        case "Ricerca Tramite Codice" -> this.loadContent("SearchSignedContractByCode");
                    }
                    break;
                case "Farmaco":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateDrug");
                        case "Aggiorna" -> this.loadContent("UpdateDrug");
                        case "Elimina" -> this.loadContent("DeleteDrug");
                        case "Visualizza" -> this.loadContent("DrugsView");
                        case "Ricerca Tramite Codice" -> this.loadContent("DrugSearchByCode");
                    }
                    break;
                case "Terapia":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateTherapy");
                        case "Elimina" -> this.loadContent("DeleteTherapy");
                        case "Visualizza" -> this.loadContent("TherapyView");
                        case "Ricerca Tramite Codice" -> this.loadContent("TherapySearchCode");
                    }
                    break;
                case "Farmaco Terapia":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateTherapyDrugs");
                        case "Aggiorna" -> this.loadContent("UpdateTherapyDrugs");
                        case "Elimina" -> this.loadContent("DeleteTherapyDrug");
                        case "Visualizza" -> this.loadContent("TherapyDrugView");
                        case "Ricerca Tramite Codice" -> this.loadContent("TherapyDrugByCode");
                    }
                    break;
                case "Beni Strumentali":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateCapitalGood");
                        case "Aggiorna" -> this.loadContent("UpdateCapitalGood");
                        case "Elimina" -> this.loadContent("DeleteCapitalGood");
                        case "Visualizza" -> this.loadContent("CapitalGoodsView");
                        case "Ricerca Tramite Codice" -> this.loadContent("SearchGoodByCode");
                    }
                    break;
                case "Cartella Clinica":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateMedicalRecords");
                        case "Aggiorna" -> this.loadContent("UpdateMedicalRecords");
                        case "Elimina" -> this.loadContent("DeleteMedicalRecord");
                        case "Visualizza" -> this.loadContent("MedicalRecordsView");
                        case "Ricerca Tramite Codice" -> this.loadContent("MedicalRecordSearchByCode");
                    }
                    break;
                case "Ospitazione":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateHosting");
                        case "Aggiorna" -> this.loadContent("UpdateHosting");
                        case "Elimina" -> this.loadContent("DeleteHosting");
                        case "Visualizza" -> this.loadContent("HostingView");
                        case "Ricerca Tramite Codice" -> this.loadContent("SearchHostingByCode");
                    }
                    break;
                case "Assumere Terapia":
                    switch (currentOperationSelection) {
                        case "Crea" -> this.loadContent("CreateTakeTherapy");
                        case "Aggiorna" -> this.loadContent("UpdateTakeTherapy");
                        case "Elimina" -> this.loadContent("DeleteTakeTherapy");
                        case "Visualizza" -> this.loadContent("TakeTherapyView");
                        case "Ricerca Tramite Codice" -> this.loadContent("TakeTherapySearchCode");
                    }
                    break;
                default:
                    this.contentPane.getChildren().clear();
                    break;
            }

        } catch (NullPointerException e) {
            this.contentPane.getChildren().clear();
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
