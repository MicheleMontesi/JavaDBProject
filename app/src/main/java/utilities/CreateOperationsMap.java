package utilities;

import java.util.*;

import static java.util.Map.entry;

public class CreateOperationsMap {

    protected static final List<String> entities = new ArrayList<>(Arrays.asList("Dipendente", "Paziente", "Unita' Operativa",
            "Farmaco", "Beni Strumentali", "Farmaco Terapia",
            "Terapia", "Cartella Clinica", "Ospitazione",
            "Tipologia Contratto", "Contratto Stipulato",
            "Attestato Acquisito", "Tipologia Attestato",
            "Turno", "Assumere Terapia"));

    protected static final Map<String, String> entitiesMap = Map.ofEntries(
            entry("Dipendente", "Worker"),
            entry("Paziente", "Patient"),
            entry("Unita' Operativa", "OpUnit"),
            entry("Farmaco", "Drug"),
            entry("Beni Strumentali", "CapitalGood"),
            entry("Farmaco Terapia", "TherapyDrug"),
            entry("Terapia", "Therapy"),
            entry("Cartella Clinica", "MedicalRecords"),
            entry("Ospitazione", "Hosting"),
            entry("Tipologia Contratto", "ContractType"),
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

        permittedOp.get("Beni Strumentali").addAll(Arrays.asList("Visualizza Automezzi", "Visualizza Attrezzature"));

        return permittedOp;
    }
}
