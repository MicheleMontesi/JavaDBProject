package db.tables;

import db.Table;
import model.TakeTherapy;
import utilities.DateConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TakeTherapiesTables implements Table<TakeTherapy, String> {

    protected static final String ASSUMERE = "assumere_terapia";
    private final Connection connection;

    public TakeTherapiesTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return ASSUMERE;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE assumere_terapia (" +
                            "  CodiceFiscale char(16) NOT NULL," +
                            "  CodiceTerapia int NOT NULL," +
                            "  PRIMARY KEY (CodiceTerapia,CodiceFiscale)," +
                            "  UNIQUE KEY ID_ASSUMERE_TERAPIA_IND (CodiceTerapia,CodiceFiscale)," +
                            "  KEY FKASS_PAZ_IND (CodiceFiscale)," +
                            "  CONSTRAINT FKASS_PAZ_FK FOREIGN KEY (CodiceFiscale) REFERENCES paziente (CodiceFiscale)," +
                            "  CONSTRAINT FKASS_TER FOREIGN KEY (CodiceTerapia) REFERENCES terapia (CodiceTerapia)" +
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
            statement.executeQuery("DROP TABLE " + ASSUMERE);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<TakeTherapy>> findByCode(String code) {
        final String query = "SELECT * FROM " +
                ASSUMERE + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TakeTherapy> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + ASSUMERE);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(TakeTherapy takeTherapy) {
        final String query = "INSERT INTO " + ASSUMERE + "(codicefiscale, codiceterapia) " +
                "VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, takeTherapy.fiscalCode());
            statement.setInt(2, takeTherapy.therapyId());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(TakeTherapy updatedValue) {
        final String query = "UPDATE " + ASSUMERE + " SET " +
                "CodiceTerapia = ? " +
                "WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, updatedValue.therapyId());
            statement.setString(2, updatedValue.fiscalCode());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String fiscalCode) {
        final String query = "DELETE FROM " + ASSUMERE + " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, fiscalCode);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

    }

    @Override
    public List<TakeTherapy> readFromResultSet(ResultSet resultSet) {
        final List<TakeTherapy> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String fiscalCode = resultSet.getString("CodiceFiscale");
                final int therapyId = resultSet.getInt("CodiceTerapia");

                final TakeTherapy takeTherapy = new TakeTherapy(fiscalCode, therapyId);
                list.add(takeTherapy);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
