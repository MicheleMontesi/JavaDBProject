package utilities;

import java.util.*;

import static java.util.Map.entry;

public class CreateOperationsMap {

    protected static final List<String> entities = new ArrayList<>(Arrays.asList(
            "Dipendente",
            "Farmaco",
            "Paziente",
            "Terapia",
            "Tipologia Contratto",
            "Unita' Operativa",
            "Beni Strumentali",
            "Farmaco Terapia",
            "Cartella Clinica",
            "Ospitazione",
            "Contratto Stipulato",
            "Tipologia Attestato",
            "Attestato Acquisito",
            "Turno",
            "Assumere Terapia"));

    protected static final Map<String, String> entitiesMap = Map.ofEntries(
            entry("Dipendente", "Worker"),
            entry("Farmaco", "Drug"),
            entry("Paziente", "Patient"),
            entry("Terapia", "Therapy"),
            entry("Tipologia Contratto", "ContractType"),
            entry("Unita' Operativa", "OpUnit"),
            entry("Beni Strumentali", "CapitalGood"),
            entry("Farmaco Terapia", "TherapyDrug"),
            entry("Cartella Clinica", "MedicalRecords"),
            entry("Ospitazione", "Hosting"),
            entry("Contratto Stipulato", "SignedContract"),
            entry("Attestato Acquisito", "CertificateAcquired"),
            entry("Tipologia Attestato", "CertificateType"),
            entry("Turno", "Shift"),
            entry("Assumere Terapia", "TakeTherapy")
    );

    private static final List<String> defaultOperations = new ArrayList<>(Arrays.asList("Crea", "Aggiorna", "Elimina",
            "Visualizza", "Ricerca Tramite Codice"));

    public static Map<String, List<String>> initOperationMap() {
        final Map<String, List<String>> permittedOp = new TreeMap<>();
        entities.forEach(e -> permittedOp.put(e, new ArrayList<>(defaultOperations)));

        permittedOp.get("Assumere Terapia").remove("Aggiorna");
        permittedOp.get("Attestato Acquisito").remove("Aggiorna");
        permittedOp.get("Ospitazione").add("Ricerca Per Anno");
        permittedOp.get("Unita' Operativa").add("Ricerca Strutture Piene");
        permittedOp.get("Dipendente").add("Ricerca Tramite Contratto");
        permittedOp.get("Dipendente").add("Ricerca Tramite Giorno Del Turno");

        return permittedOp;
    }
}
