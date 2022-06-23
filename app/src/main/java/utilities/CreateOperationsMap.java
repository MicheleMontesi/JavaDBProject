package utilities;

import java.util.*;

public class CreateOperationsMap {

    private static final List<String> entities = new ArrayList<>(Arrays.asList("Dipendente", "Paziente", "Unita' Operativa",
            "Farmaco", "Beni Strumentali", "Farmaco Terapia",
            "Terapia", "Cartella Clinica", "Ospitazione",
            "Tipologia Contratto", "Contratto Stipulato",
            "Attestato Acquisito", "Tipologia Attestato",
            "Turno"));
    private static final List<String> defaultOperations = new ArrayList<>(Arrays.asList("Crea", "Elimina", "Visualizza",
            "Ricerca Tramite Parametro"));

    public static Map<String, List<String>> initOperationMap() {
        final Map<String, List<String>> permittedOp = new TreeMap<>();
        entities.forEach(e -> permittedOp.put(e, new ArrayList<>(defaultOperations)));

        permittedOp.get("Dipendente").addAll(Arrays.asList("Visualizza Turni", "Visualizza Attestati",
                "Visualizza Contratto", "Visualizza Farmaci Somministrati"));
        permittedOp.get("Paziente").addAll(Arrays.asList("Visualizza Cartella Clinica",
                "Visualizza Inizio/Fine Ospitazione", "Visualizza Terapia"));
        permittedOp.get("Unita' Operativa").addAll(Arrays.asList("Visualizza Turni", "Visualizza Beni Strumentali",
                "Visualizza Ospitazioni"));
        permittedOp.get("Beni Strumentali").addAll(Arrays.asList("Visualizza Automezzi", "Visualizza Attrezzature"));

        return permittedOp;
    }
}
