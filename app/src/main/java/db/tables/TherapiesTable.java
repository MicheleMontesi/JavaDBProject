package db.tables;

import db.Table;
import model.Therapy;
import utilities.DateConverter;

import java.sql.*;
import java.util.Date;
import java.util.*;

import static utilities.checkers.CommonCheckers.nonErasableCheck;

public class TherapiesTable implements Table<Therapy, String> {

    protected static final String TERAPIA = "terapia";
    private final Connection connection;

    public TherapiesTable(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TERAPIA;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE terapia (" +
                            "  CodiceTerapia int NOT NULL," +
                            "  DataInizio date NOT NULL," +
                            "  PRIMARY KEY (CodiceTerapia)," +
                            "  UNIQUE KEY ID_TERAPIA_IND (CodiceTerapia)" +
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
            statement.executeQuery("DROP TABLE " + TERAPIA);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    public Optional<List<Therapy>> findByCode(String code) {
        final String query = "SELECT * FROM " +
                TERAPIA + " WHERE CodiceTerapia = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Therapy> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TERAPIA);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public void save(Therapy therapy) {
        final String query = "INSERT INTO " + TERAPIA + "(codiceterapia, DataInizio) " +
                "VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, therapy.therapyId());
            statement.setDate(2, DateConverter.dateToSqlDate(therapy.creationDate()));
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(Therapy therapy) {
        final String query = "UPDATE " + TERAPIA + " SET " +
                "DataInizio = ? " +
                "WHERE CodiceTerapia = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, DateConverter.dateToSqlDate(therapy.creationDate()));
            statement.setInt(2, therapy.therapyId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String therapyId) {
        final String query = "DELETE FROM " + TERAPIA +
                " WHERE CodiceTerapia = ?";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, therapyId);
            statement.executeUpdate();
        } catch (SQLException e) {
            nonErasableCheck();
        }
    }

    @Override
    public List<Therapy> readFromResultSet(ResultSet resultSet) {
        final List<Therapy> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int therapyId = resultSet.getInt("CodiceTerapia");
                final Date beginDate = resultSet.getDate("DataInizio");

                final Therapy therapy = new Therapy(therapyId, beginDate);
                list.add(therapy);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
