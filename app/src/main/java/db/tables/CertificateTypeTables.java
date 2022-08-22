package db.tables;

import db.Table;
import model.CertificateType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static utilities.checkers.CommonCheckers.nonErasableCheck;

public class CertificateTypeTables implements Table<CertificateType, String> {

    protected static final String TIPOLOGIA_ATTESTATO = "tipologia_attestato";
    private final Connection connection;

    public CertificateTypeTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TIPOLOGIA_ATTESTATO;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE tipologia_attestato (" +
                            "  Nome char(30) NOT NULL," +
                            "  CreditiECM int NOT NULL," +
                            "  PRIMARY KEY (Nome)," +
                            "  UNIQUE KEY ID_TIPOLOGIA_ATTESTATO_IND (Nome)" +
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
            statement.executeUpdate("DROP TABLE " + TIPOLOGIA_ATTESTATO);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<CertificateType>> findByCode(String name) {
        final String query = "SELECT * FROM " +
                TIPOLOGIA_ATTESTATO + " WHERE Nome = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, name);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CertificateType> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TIPOLOGIA_ATTESTATO);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(CertificateType certificateType) {
        final String query = "INSERT INTO " + TIPOLOGIA_ATTESTATO + "(Nome, CreditiECM) " +
                "VALUES (?, ?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, certificateType.name());
            statement.setInt(2, certificateType.ecmCredits());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(CertificateType certificateType) {
        final String query = "UPDATE " + TIPOLOGIA_ATTESTATO + " SET " +
                "CreditiECM = ? " +
                "WHERE Nome = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, certificateType.ecmCredits());
            statement.setString(2, certificateType.name());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String name) {
        final String query = "DELETE FROM " + TIPOLOGIA_ATTESTATO + " WHERE Nome = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            nonErasableCheck();
        }
    }

    @Override
    public List<CertificateType> readFromResultSet(final ResultSet resultSet) {
        final List<CertificateType> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String name = resultSet.getString("Nome");
                final int ecmCredits = resultSet.getInt("CreditiECM");

                final CertificateType contractType = new CertificateType(name, ecmCredits);
                list.add(contractType);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
