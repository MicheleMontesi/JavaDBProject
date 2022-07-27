package db.tables;

import db.Table;
import model.ContractType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static utilities.checkers.CommonCheckers.nonErasableCheck;

public class ContractTypeTables implements Table<ContractType, String> {

    protected static final String TIPOLOGIA_CONTRATTO = "tipologia_contratto";
    private final Connection connection;

    public ContractTypeTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TIPOLOGIA_CONTRATTO;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE tipologia_contratto (" +
                            "   Nome char(20) NOT NULL," +
                            "   OreContrattuali int NOT NULL," +
                            "   PRIMARY KEY (Nome)," +
                            "   UNIQUE KEY ID_TIPOLOGIA_CONTRATTO_IND (Nome)" +
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
            statement.executeQuery("DROP TABLE " + TIPOLOGIA_CONTRATTO);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<ContractType>> findByCode(String name) {
        final String query = "SELECT * FROM " +
                TIPOLOGIA_CONTRATTO + " WHERE Nome = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, name);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ContractType> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TIPOLOGIA_CONTRATTO);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(ContractType contractType) {
        final String query = "INSERT INTO " + TIPOLOGIA_CONTRATTO + "(nome, orecontrattuali) " +
                "VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, contractType.name());
            statement.setInt(2, contractType.contractualHours());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(ContractType contractType) {
        final String query = "UPDATE " + TIPOLOGIA_CONTRATTO + " SET " +
                "OreContrattuali = ? " +
                "WHERE Nome = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, contractType.contractualHours());
            statement.setString(2, contractType.name());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String name) {
        final String query = "DELETE FROM " + TIPOLOGIA_CONTRATTO + " WHERE Nome = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            nonErasableCheck();
        }
    }

    @Override
    public List<ContractType> readFromResultSet(final ResultSet resultSet) {
        final List<ContractType> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String name = resultSet.getString("Nome");
                final int contractualHours = resultSet.getInt("OreContrattuali");

                final ContractType contractType = new ContractType(name, contractualHours);
                list.add(contractType);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
