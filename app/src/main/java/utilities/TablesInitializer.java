package utilities;

import db.Table;
import db.tables.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utilities.CreateOperationsMap.entities;

public class TablesInitializer {

    private static final ConnectionProvider connectionProvider = new ConnectionProvider("root",
            "o6*&GstbGajcf&x5", "cooperativasanitaria");

    private static final Connection connection = connectionProvider.getMySQLConnection();

    public static void init() {
        var list = initTables();
        var i = 0;
        for (var entity : entities) {
            if (!check(replaceString(entity))) {
                list.get(i).createTable();
                System.out.println("Creazione: " + entity);
            } else {
                System.out.println("Presente: " + entity);
            }
            i++;
        }
    }

    private static boolean check(String entity) {
        final String query = "SHOW TABLES LIKE ?";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity);
            final ResultSet resultSet = statement.executeQuery();
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            return false;
        }
    }

    private static String replaceString(String entity) {
        return Normalizer.normalize(entity, Normalizer.Form.NFD)
                .toLowerCase()
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("'", "")
                .replaceAll("\\s+", "_");
    }

    private static List<Table<?, String>> initTables() {
        return new ArrayList<>(Arrays.asList(
                new WorkersTables(connection),
                new DrugsTables(connection),
                new PatientsTables(connection),
                new TherapiesTable(connection),
                new ContractTypeTables(connection),
                new OperatingUnitTables(connection),
                new CapitalGoodsTables(connection),
                new TherapyDrugsTable(connection),
                new MedicalRecordsTables(connection),
                new HostingTables(connection),
                new SignedContractsTables(connection),
                new CertificateTypeTables(connection),
                new CertificateAcquiredTables(connection),
                new ShiftsTables(connection),
                new TakeTherapiesTables(connection)
                ));
    }

    private static boolean readFromResultSet(ResultSet resultSet) {
        var counter = 0;
        try {
            while (resultSet.next()) {
                counter++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return counter > 0;
    }
}
