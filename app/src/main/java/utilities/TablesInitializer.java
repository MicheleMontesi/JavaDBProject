package utilities;

import db.Table;
import db.tables.*;

import java.sql.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utilities.CreateOperationsMap.entities;

public class TablesInitializer {

    public static void createDB() {
        final String username = "root";
        final String password = "o6*&GstbGajcf&x5";
        final String dbName = "cooperativasanitaria";

        final String uri = "jdbc:mysql://localhost:3306/";
        try {
            var con = DriverManager.getConnection(uri, username, password);
            if (con != null) {
                var rs = con.getMetaData().getCatalogs();
                while(rs.next()){
                    String catalogs = rs.getString(1);
                    if(dbName.equals(catalogs)){
                        System.out.println("DB " + dbName + " already exists");
                        return;
                    }
                }

                Statement statement = con.createStatement();
                statement.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("Created DB: " + dbName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        final ConnectionProvider connectionProvider = new ConnectionProvider();
        final Connection connection = connectionProvider.getMySQLConnection();
        var list = initTables(connection);
        var i = 0;
        for (var entity : entities) {
            if (!check(replaceString(entity), connection)) {
                list.get(i).createTable();
                System.out.println("Creazione: " + entity);
            } else {
                System.out.println("Presente: " + entity);
            }
            i++;
        }
    }

    private static boolean check(String entity, Connection connection) {
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

    private static List<Table<?, String>> initTables(Connection connection) {
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
