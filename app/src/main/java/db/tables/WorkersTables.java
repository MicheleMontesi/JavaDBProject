package db.tables;

import db.Table;
import model.Worker;
import utilities.DateConverter;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class WorkersTables implements Table<Worker, String> {
    protected static final String DIPENDENTE = "dipendente";
    private final Connection connection;

    public WorkersTables(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return DIPENDENTE;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE " + DIPENDENTE + " (" +
                            "CodiceFiscale CHAR(16) NOT NULL, " +
                            "Nome CHAR(40) NOT NULL, " +
                            "Cognome CHAR(40) NOT NULL, " +
                            "Compleanno DATE NOT NULL, " +
                            "Residenza CHAR(50) NOT NULL, " +
                            "Sesso CHAR(1) NOT NULL, " +
                            "CodiceDipendente INT NOT NULL, " +
                            "TitoloDiStudio CHAR(50) NOT NULL, " +
                            "IdoneitaAllaMansione CHAR(1) NOT NULL, " +
                            "Socio CHAR(1) NOT NULL, " +
                            "CreditiECM INT NOT NULL, " +
                            "PRIMARY KEY (CodiceFiscale), " +
                            "UNIQUE KEY SID_DIPENDENTE_ID (CodiceDipendente), " +
                            "UNIQUE KEY SID_DIPENDENTE_IND (CodiceDipendente), " +
                            "UNIQUE KEY ID_DIPENDENTE_IND (CodiceFiscale)" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci"
            );
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean dropTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeQuery("DROP TABLE " + DIPENDENTE);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<Worker>> findByCode(String code) {
        final String query = "SELECT * FROM " + DIPENDENTE + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Worker> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DIPENDENTE);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Worker worker) {
        final String query = "INSERT INTO " + DIPENDENTE + "(CodiceFiscale, Nome, Cognome, Compleanno, Residenza, " +
                "Sesso, CodiceDipendente, TitoloDiStudio, IdoneitaAllaMansione, Socio, CreditiECM) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, worker.fiscalCode());
            statement.setString(2, worker.name());
            statement.setString(3, worker.surname());
            statement.setDate(4, DateConverter.dateToSqlDate(worker.birthDay()));
            statement.setString(5, worker.residence());
            statement.setString(6, worker.gender());
            statement.setInt(7, worker.workerId());
            statement.setString(8, worker.edQualification());
            statement.setString(9, worker.suitability() ? "Y" : "N");
            statement.setString(10, worker.partner() ? "Y" : "N");
            statement.setInt(11, worker.ECMCredits());
            statement.executeUpdate();
        } catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean update(Worker worker) {
        final String query = "UPDATE " + DIPENDENTE + " SET " +
                "Nome = ?, " +
                "Cognome = ?, " +
                "Compleanno = ?, " +
                "Residenza = ?, " +
                "Sesso = ?, " +
                "CodiceDipendente = ?, " +
                "TitoloDiStudio = ?, " +
                "IdoneitaAllaMansione = ?, " +
                "Socio = ?, " +
                "CreditiECM = ? " +
                "WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, worker.name());
            statement.setString(2, worker.surname());
            statement.setDate(3, DateConverter.dateToSqlDate(worker.birthDay()));
            statement.setString(4, worker.residence());
            statement.setString(5, worker.gender());
            statement.setInt(6, worker.workerId());
            statement.setString(7, worker.edQualification());
            statement.setString(8, worker.suitability() ? "Y" : "N");
            statement.setString(9, worker.partner() ? "Y" : "N");
            statement.setInt(10, worker.ECMCredits());
            statement.setString(11, worker.fiscalCode());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(final String CodiceFiscale) {
        final String query = "DELETE FROM " + DIPENDENTE + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setString(1, CodiceFiscale);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Worker> readFromResultSet(final ResultSet resultSet) {
        final List<Worker> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String fiscalCode = resultSet.getString("CodiceFiscale");
                final String name = resultSet.getString("Nome");
                final String surname = resultSet.getString("Cognome");
                final Date birthDay = resultSet.getDate("Compleanno");
                final String residence = resultSet.getString("Residenza");
                final String gender = resultSet.getString("Sesso");
                final int workerId = resultSet.getInt("CodiceDipendente");
                final String edQualification = resultSet.getString("TitoloDiStudio");
                final boolean suitability = resultSet.getString("IdoneitaAllaMansione").equals("Y");
                final boolean partner = resultSet.getString("Socio").equals("Y");
                final int ECMCredits = resultSet.getInt("CreditiECM");

                final Worker worker = new Worker(fiscalCode, name, surname, birthDay, residence, gender, workerId,
                        edQualification, suitability, partner, ECMCredits);
                list.add(worker);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
