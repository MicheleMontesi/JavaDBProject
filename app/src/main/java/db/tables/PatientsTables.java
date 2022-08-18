package db.tables;

import db.Table;
import model.Hosting;
import model.Patient;
import utilities.DateConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static utilities.checkers.CommonCheckers.nonErasableCheck;

public class PatientsTables implements Table<Patient, String> {

    protected static final String PAZIENTE = "paziente";
    private final Connection connection;

    public PatientsTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return PAZIENTE;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE paziente (" +
                            "   CodiceFiscale char(16) NOT NULL," +
                            "   Nome char(15) NOT NULL," +
                            "   Cognome char(15) NOT NULL," +
                            "   Compleanno date NOT NULL," +
                            "   Residenza char(50) NOT NULL," +
                            "   Sesso char(1) NOT NULL," +
                            "   CodicePaziente int NOT NULL," +
                            "   DocumentazionePrivacy char(1) NOT NULL," +
                            "   ConsensoInformatoAlTrattamento char(1) NOT NULL," +
                            "   AccettazioneRegolamento char(1) NOT NULL," +
                            "   PRIMARY KEY (CodiceFiscale)," +
                            "   UNIQUE KEY SID_PAZIENTE_ID (CodicePaziente)," +
                            "   UNIQUE KEY SID_PAZIENTE_IND (CodicePaziente)," +
                            "   UNIQUE KEY ID_PAZIENTE_IND (CodiceFiscale)" +
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
            statement.executeQuery("DROP TABLE " + PAZIENTE);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<Patient>> findByCode(String code) {
        final String query = "SELECT * FROM " + PAZIENTE + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<List<Patient>> findPatientByYear(int year, PatientsTables patientsTables) {
        final String query = "SELECT * " +
                "FROM paziente p LEFT JOIN ospitazione o on p.CodiceFiscale = o.CodiceFiscale " +
                "WHERE YEAR(o.DataInizio) = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, year);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Patient> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + PAZIENTE);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Patient patient) {
        final String query = "INSERT INTO " + PAZIENTE + "(CodiceFiscale, Nome, Cognome, Compleanno, Residenza, " +
                "Sesso, CodicePaziente, DocumentazionePrivacy, ConsensoInformatoAlTrattamento, AccettazioneRegolamento) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, patient.fiscalCode());
            statement.setString(2, patient.name());
            statement.setString(3, patient.surname());
            statement.setDate(4, DateConverter.dateToSqlDate(patient.birthday()));
            statement.setString(5, patient.residence());
            statement.setString(6, patient.gender());
            statement.setInt(7, patient.patientId());
            statement.setString(8, patient.privacyDocumentation() ? "Y" : "N");
            statement.setString(9, patient.consentTreatment() ? "Y" : "N");
            statement.setString(10, patient.acceptRules() ? "Y" : "N");
            statement.executeUpdate();
        } catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean update(Patient patient) {
        final String query = "UPDATE " + PAZIENTE + " SET " +
                "Nome = ?, " +
                "Cognome = ?, " +
                "Compleanno = ?, " +
                "Residenza = ?, " +
                "Sesso = ?, " +
                "CodicePaziente = ?, " +
                "DocumentazionePrivacy = ?, " +
                "ConsensoInformatoAlTrattamento = ?, " +
                "AccettazioneRegolamento = ? " +
                "WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, patient.name());
            statement.setString(2, patient.surname());
            statement.setDate(3, DateConverter.dateToSqlDate(patient.birthday()));
            statement.setString(4, patient.residence());
            statement.setString(5, patient.gender());
            statement.setInt(6, patient.patientId());
            statement.setString(7, patient.privacyDocumentation() ? "Y" : "N");
            statement.setString(8, patient.consentTreatment() ? "Y" : "N");
            statement.setString(9, patient.acceptRules() ? "Y" : "N");
            statement.setString(10, patient.fiscalCode());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String fiscalCode) {
        final String query = "DELETE FROM " + PAZIENTE + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setString(1, fiscalCode);
            statement.executeUpdate();
        } catch (SQLException e) {
            nonErasableCheck();
        }
    }

    @Override
    public List<Patient> readFromResultSet(ResultSet resultSet) {
        final List<Patient> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String fiscalCode = resultSet.getString("CodiceFiscale");
                final String name = resultSet.getString("Nome");
                final String surname = resultSet.getString("Cognome");
                final Date birthDay = resultSet.getDate("Compleanno");
                final String residence = resultSet.getString("Residenza");
                final String gender = resultSet.getString("Sesso");
                final int patientId = resultSet.getInt("CodicePaziente");
                final boolean privacyDocumentation = resultSet.getString("DocumentazionePrivacy").equals("Y");
                final boolean consentTreatment = resultSet.getString("ConsensoInformatoAlTrattamento").equals("Y");
                final boolean acceptRules = resultSet.getString("AccettazioneRegolamento").equals("Y");

                final Patient patient = new Patient(fiscalCode, name, surname, birthDay, residence, gender, patientId,
                        privacyDocumentation, consentTreatment, acceptRules);
                list.add(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
