package db.tables;

import db.Table;
import model.MedicalRecords;
import model.Shift;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MedicalRecordsTable implements Table<MedicalRecords, String> {

    protected static final String CARTELLA = "cartella_clinica";
    private final Connection connection;

    public MedicalRecordsTable(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return CARTELLA;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE cartella_clinica (" +
                            "   CodCartellaClinica int NOT NULL," +
                            "   CodiceFiscale char(16) NOT NULL," +
                            "   Anamnesi char(255) NOT NULL," +
                            "   Diagnosi char(255) NOT NULL," +
                            "   ProgettoRiabilitativo char(255) NOT NULL," +
                            "   PRIMARY KEY (CodCartellaClinica)," +
                            "   UNIQUE KEY FKIDENTIFICARE_ID (CodiceFiscale)," +
                            "   UNIQUE KEY ID_CARTELLA_CLINICA_IND (CodCartellaClinica)," +
                            "   UNIQUE KEY FKIDENTIFICARE_IND (CodiceFiscale)," +
                            "   CONSTRAINT FKIDENTIFICARE_FK FOREIGN KEY (CodiceFiscale) REFERENCES paziente (CodiceFiscale)" +
                            " ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci"
            );
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean dropTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeQuery("DROP TABLE " + CARTELLA);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<MedicalRecords>> findByCode(String code) {
        final String query = "SELECT * FROM " +
                CARTELLA + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MedicalRecords> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + CARTELLA);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(MedicalRecords medicalRecords) {
        final String query = "INSERT INTO " + CARTELLA + "(codcartellaclinica, codicefiscale, anamnesi, " +
                "diagnosi, progettoriabilitativo) VALUES (?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, medicalRecords.medicalRecordId());
            statement.setString(2, medicalRecords.fiscalCode());
            statement.setString(3, medicalRecords.anamnesis());
            statement.setString(4, medicalRecords.diagnosis());
            statement.setString(5, medicalRecords.rehabProject());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(MedicalRecords medicalRecords) {
        final String query = "UPDATE " + CARTELLA + " SET " +
                "Anamnesi = ?, " +
                "Diagnosi = ?, " +
                "ProgettoRiabilitativo = ? " +
                "WHERE CodiceFiscale = ? AND CodCartellaClinica = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, medicalRecords.anamnesis());
            statement.setString(2, medicalRecords.diagnosis());
            statement.setString(3, medicalRecords.rehabProject());
            statement.setString(4, medicalRecords.fiscalCode());
            statement.setInt(5, medicalRecords.medicalRecordId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String fiscalCode) {
        final String query = "DELETE FROM " + CARTELLA + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, fiscalCode);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    @Override
    public List<MedicalRecords> readFromResultSet(final ResultSet resultSet) {
        final List<MedicalRecords> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int medicalRecordId = resultSet.getInt("CodCartellaClinica");
                final String fiscalCode = resultSet.getString("CodiceFiscale");
                final String anamnesis = resultSet.getString("Anamnesi");
                final String diagnosis = resultSet.getString("Diagnosi");
                final String rehabProject = resultSet.getString("ProgettoRiabilitativo");

                final MedicalRecords medicalRecords = new MedicalRecords(medicalRecordId, fiscalCode, anamnesis,
                        diagnosis, rehabProject);
                list.add(medicalRecords);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
