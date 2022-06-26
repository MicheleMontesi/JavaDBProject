package db.tables;

import db.Table;
import model.Shift;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;

public class ShiftsTable implements Table<Shift, String> {

    public static final String TURNO = "turno";
    private final Connection connection;

    public ShiftsTable(Connection connection) {
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
                            "GiornoSettimana CHAR(10) NOT NULL PRIMARY KEY, " +
                            "OraInizio TIME NOT NULL PRIMARY KEY , " +
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
    public Optional<List<Shift>> findByFiscalCode(String fiscalCode) {
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
    public List<Shift> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TURNO);
            return readTurnFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Shift shift) {
        final String query = "INSERT INTO " + TURNO + "(CodiceFiscale, GiornoSettimana, OraInizio, OraFine, CodiceUnita) " +
                "VALUES (?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, shift.fiscalCode());
            statement.setString(2, shift.dayOfTheWeek());
            statement.setTime(3, Time.valueOf(shift.beginTime()));
            statement.setTime(4, Time.valueOf(shift.endTime()));
            statement.setString(5, shift.unitId());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(Shift shift) {
        final String query = "UPDATE " + TURNO + " SET " +
                "OraInizio = ?, " +
                "OraFine = ?, " +
                "CodiceUnita = ? " +
                "WHERE CodiceFiscale = ? AND GiornoSettimana = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setTime(1, Time.valueOf(shift.beginTime()));
            statement.setTime(2, Time.valueOf(shift.endTime()));
            statement.setString(3, shift.unitId());
            statement.setString(4, shift.fiscalCode());
            statement.setString(5, shift.dayOfTheWeek());
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

    private List<Shift> readTurnFromResultSet(final ResultSet resultSet) {
        final List<Shift> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String workerFiscalCode = resultSet.getString("CodiceFiscale");
                final String dayOfTheWeek = resultSet.getString("GiornoSettimana");
                final LocalTime beginTime = resultSet.getTime("OraInizio").toLocalTime();
                final LocalTime endTime = resultSet.getTime("OraFine").toLocalTime();
                final String unitId = resultSet.getString("CodiceUnita");

                final Shift shift = new Shift(workerFiscalCode, dayOfTheWeek, beginTime, endTime, unitId);
                list.add(shift);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
