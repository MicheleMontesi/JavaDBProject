package utilities;

import db.tables.*;

import java.util.Arrays;

public class DropUtils {

    private static final ConnectionProvider connectionProvider = new ConnectionProvider();

    public static void dropAll() {
        var tableList = Arrays.asList(
                new TakeTherapiesTables(connectionProvider.getMySQLConnection()),
                new ShiftsTables(connectionProvider.getMySQLConnection()),
                new CertificateAcquiredTables(connectionProvider.getMySQLConnection()),
                new CertificateTypeTables(connectionProvider.getMySQLConnection()),
                new SignedContractsTables(connectionProvider.getMySQLConnection()),
                new HostingTables(connectionProvider.getMySQLConnection()),
                new MedicalRecordsTables(connectionProvider.getMySQLConnection()),
                new TherapyDrugsTable(connectionProvider.getMySQLConnection()),
                new CapitalGoodsTables(connectionProvider.getMySQLConnection()),
                new OperatingUnitTables(connectionProvider.getMySQLConnection()),
                new ContractTypeTables(connectionProvider.getMySQLConnection()),
                new TherapiesTable(connectionProvider.getMySQLConnection()),
                new PatientsTables(connectionProvider.getMySQLConnection()),
                new DrugsTables(connectionProvider.getMySQLConnection()),
                new WorkersTables(connectionProvider.getMySQLConnection()));
        for (var table : tableList) {
            var ret = table.dropTable();
            System.out.println(ret);
        }
    }
}
