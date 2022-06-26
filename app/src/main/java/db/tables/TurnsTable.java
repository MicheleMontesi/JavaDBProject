package db.tables;

import db.Table;
import model.Turn;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;

public class TurnsTable implements Table<Turn, String> {

    public static final String TURNO = "turno";
    private final Connection connection;

    public TurnsTable(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TURNO;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE " + TURNO + " (" +
                            "CodiceFiscale CHAR(16) NOT NULL PRIMARY KEY, " +
                            "GiornoSettimana CHAR(10), " +
                            "OraInizio TIME, " +
                            "OraFine TIME, " +
                            "CodiceUnita CHAR(5)" +
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
            statement.executeQuery("DROP TABLE " + TURNO);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<Turn>> findByFiscalCode(String fiscalCode) {
        final String query = "SELECT * FROM " +
                TURNO + " t join " + WorkersTable.DIPENDENTE + " d on (t.CodiceFiscale = d.CodiceFiscale) " +
                "WHERE d.CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, fiscalCode);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readTurnFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Turn> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TURNO);
            return readTurnFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Turn turn) {
        final String query = "INSERT INTO " + TURNO + "(CodiceFiscale, GiornoSettimana, OraInizio, OraFine, CodiceUnita) " +
                "VALUES (?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, turn.workerFiscalCode());
            statement.setString(2, turn.dayOfTheWeek());
            statement.setTime(3, Time.valueOf(turn.beginTime()));
            statement.setTime(4, Time.valueOf(turn.endTime()));
            statement.setString(5, turn.unitId());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(Turn turn) {
        final String query = "UPDATE " + TURNO + " SET " +
                "OraInizio = ?, " +
                "OraFine = ?, " +
                "CodiceUnita = ? " +
                "WHERE CodiceFiscale = ? AND GiornoSettimana = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setTime(1, Time.valueOf(turn.beginTime()));
            statement.setTime(2, Time.valueOf(turn.endTime()));
            statement.setString(3, turn.unitId());
            statement.setString(4, turn.workerFiscalCode());
            statement.setString(5, turn.dayOfTheWeek());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String CodiceFiscale) {
        final String query = "DELETE FROM " + TURNO + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setString(1, CodiceFiscale);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Turn> readTurnFromResultSet(final ResultSet resultSet) {
        final List<Turn> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String workerFiscalCode = resultSet.getString("CodiceFiscale");
                final String dayOfTheWeek = resultSet.getString("GiornoSettimana");
                final LocalTime beginTime = resultSet.getTime("OraInizio").toLocalTime();
                final LocalTime endTime = resultSet.getTime("OraFine").toLocalTime();
                final String unitId = resultSet.getString("CodiceUnita");

                final Turn turn = new Turn(workerFiscalCode, dayOfTheWeek, beginTime, endTime, unitId);
                list.add(turn);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
