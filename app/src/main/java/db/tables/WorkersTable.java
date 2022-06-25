package db.tables;

import db.Table;
import model.Worker;
import utilities.DateConverter;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class WorkersTable implements Table<Worker, String> {
    public static final String TABLE_NAME = "dipendente";
    private final Connection connection;

    public WorkersTable(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE " + TABLE_NAME + " (" +
                            "CodiceFiscale CHAR(16) NOT NULL PRIMARY KEY, " +
                            "Nome CHAR(40), " +
                            "Cognome CHAR(40), " +
                            "Compleanno DATE, " +
                            "Residenza CHAR(40), " +
                            "Sesso CHAR(1), " +
                            "CodiceDipendente INT, " +
                            "TitoloDiStudio CHAR(40), " +
                            "IdoneitaAllaMansione CHAR(1), " +
                            "Socio CHAR(1), " +
                            "CreditiECM INT" +
                            ")"
            );
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean dropTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeQuery("DROP TABLE " + TABLE_NAME);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<Worker> findByFiscalCode(String fiscalCode) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, fiscalCode);
            final ResultSet resultSet = statement.executeQuery();
            return readWorkersFromResultSet(resultSet).stream().findFirst();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Worker> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readWorkersFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(Worker worker) { //togli il optional alla data
        final String query = "INSERT INTO " + TABLE_NAME + "(CodiceFiscale, Nome, Cognome, Compleanno, Residenza, " +
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
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean update(Worker worker) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
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
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final String CodiceFiscale) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setString(1, CodiceFiscale);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Worker> readWorkersFromResultSet(final ResultSet resultSet) {
        final List<Worker> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String fiscalCode = resultSet.getString("CodiceFiscale");
                final String name = resultSet.getString("Nome");
                final String surname = resultSet.getString("Cognome");
                final Date birthDay = DateConverter.sqlDateToDate(resultSet.getDate("Compleanno"));
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
