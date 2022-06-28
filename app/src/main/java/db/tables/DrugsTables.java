package db.tables;

import db.Table;
import model.ContractType;
import model.Drug;
import utilities.DateConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DrugsTables implements Table<Drug, String> {

    protected static final String FARMACO = "farmaco";
    private final Connection connection;

    public DrugsTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return FARMACO;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE farmaco (" +
                            "   CodFarmaco int NOT NULL," +
                            "   Nome char(20) NOT NULL," +
                            "   CasaFarmaceutica char(50) NOT NULL," +
                            "   DataAcquisto date NOT NULL," +
                            "   DataScadenza date NOT NULL," +
                            "   Quantita int NOT NULL," +
                            "   PRIMARY KEY (CodFarmaco)," +
                            "   UNIQUE KEY ID_FARMACO_IND (CodFarmaco)" +
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
            statement.executeQuery("DROP TABLE " + FARMACO);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<Drug>> findByCode(String code) {
        final String query = "SELECT * FROM " +
                FARMACO + " WHERE CodFarmaco = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readContractFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Drug> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + FARMACO);
            return readContractFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Drug drug) {
        final String query = "INSERT INTO " + FARMACO + "(codfarmaco, nome, casafarmaceutica, dataacquisto, " +
                "datascadenza, quantita) VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, drug.drugId());
            statement.setString(2, drug.name());
            statement.setString(3, drug.pharmaCompany());
            statement.setDate(4, DateConverter.dateToSqlDate(drug.purchaseDate()));
            statement.setDate(5, DateConverter.dateToSqlDate(drug.expirationDate()));
            statement.setInt(6, drug.quantity());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(Drug drug) {
        final String query = "UPDATE " + FARMACO + " SET " +
                "Nome = ?, " +
                "CasaFarmaceutica = ?, " +
                "DataAcquisto = ?, " +
                "DataScadenza = ?, " +
                "Quantita = ? " +
                "WHERE CodFarmaco = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, drug.name());
            statement.setString(2, drug.pharmaCompany());
            statement.setDate(3, DateConverter.dateToSqlDate(drug.purchaseDate()));
            statement.setDate(4, DateConverter.dateToSqlDate(drug.expirationDate()));
            statement.setInt(5, drug.quantity());
            statement.setInt(6, drug.drugId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String codFarmaco) {
        final String query = "DELETE FROM " + FARMACO + " WHERE CodFarmaco = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, codFarmaco);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Drug> readContractFromResultSet(final ResultSet resultSet) {
        final List<Drug> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int codFarmaco = resultSet.getInt("CodFarmaco");
                final String name = resultSet.getString("Nome");
                final String pharmaCompany = resultSet.getString("CasaFarmaceutica");
                final Date purchaseDate = resultSet.getDate("DataAcquisto");
                final Date expirationDate = resultSet.getDate("DataScadenza");
                final int quantity = resultSet.getInt("Quantita");

                final Drug drug = new Drug(codFarmaco, name, pharmaCompany, purchaseDate, expirationDate, quantity);
                list.add(drug);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
