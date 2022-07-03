package db.tables;

import db.Table;
import model.CapitalGood;
import utilities.DateConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CapitalGoodsTables implements Table<CapitalGood, String> {

    protected static final String BENI = "beni_strumentali";
    private final Connection connection;

    public CapitalGoodsTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return BENI;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE beni_strumentali (" +
                            "   CodiceUnita char(5) NOT NULL," +
                            "   CodBene int NOT NULL," +
                            "   DataAcquisto date NOT NULL," +
                            "   ProssimaManutenzione date NOT NULL," +
                            "   Automezzo char(1) NOT NULL," +
                            "   NomeAttrezzo char(20) DEFAULT NULL," +
                            "   Targa char(10) DEFAULT NULL," +
                            "   Tipologia char(20) DEFAULT NULL," +
                            "   ScadenzaAssicurazione date DEFAULT NULL," +
                            "   PRIMARY KEY (CodiceUnita,CodBene)," +
                            "   UNIQUE KEY ID_BENI_STRUMENTALI_IND (CodiceUnita,CodBene)," +
                            "   CONSTRAINT FKPOSSEDERE FOREIGN KEY (CodiceUnita) REFERENCES unita_operativa (CodiceUnita)" +
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
            statement.executeQuery("DROP TABLE " + BENI);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<CapitalGood>> findByCode(String unitId) {
        final String query = "SELECT * FROM " + BENI + " WHERE CodiceUnita = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, unitId);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CapitalGood> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + BENI);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(CapitalGood good) {
        final String query = "INSERT INTO " + BENI + "(CodiceUnita, CodBene, DataAcquisto, ProssimaManutenzione, " +
                "Automezzo, NomeAttrezzo, Targa, Tipologia, ScadenzaAssicurazione) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, good.unitId());
            statement.setInt(2, good.goodId());
            statement.setDate(3, DateConverter.dateToSqlDate(good.purchaseDate()));
            statement.setDate(4, DateConverter.dateToSqlDate(good.nextMaintenance()));
            statement.setString(5, good.vehicle() ? "Y" : "N");
            statement.setString(6, good.toolName().orElse(null));
            statement.setString(7, good.licencePlate().orElse(null));
            statement.setString(8, good.typology().orElse(null));
            statement.setDate(9, good.insuranceExpiration().map(DateConverter::dateToSqlDate).orElse(null));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(CapitalGood good) {
        final String query = "UPDATE " + BENI + " SET " +
                "DataAcquisto = ?, " +
                "ProssimaManutenzione = ?, " +
                "Automezzo = ?, " +
                "NomeAttrezzo = ?, " +
                "Targa = ?, " +
                "Tipologia = ?, " +
                "ScadenzaAssicurazione = ? " +
                "WHERE CodiceUnita = ? AND CodBene = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, DateConverter.dateToSqlDate(good.purchaseDate()));
            statement.setDate(2, DateConverter.dateToSqlDate(good.nextMaintenance()));
            statement.setString(3, good.vehicle() ? "Y" : "N");
            statement.setString(4, good.toolName().orElse(null));
            statement.setString(5, good.licencePlate().orElse(null));
            statement.setString(6, good.typology().orElse(null));
            statement.setDate(7, good.insuranceExpiration().map(DateConverter::dateToSqlDate).orElse(null));
            statement.setString(8, good.unitId());
            statement.setInt(9, good.goodId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String codBene){
    }

    public void deleteByParameters(String codBene, int codUnita) {
        final String query = "DELETE FROM " + BENI + " WHERE CodBene = ? AND CodiceUnita = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setString(1, codBene);
            statement.setInt(2, codUnita);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<CapitalGood> readFromResultSet(final ResultSet resultSet) {
        final List<CapitalGood> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String unitId = resultSet.getString("CodiceUnita");
                final int goodId = resultSet.getInt("CodBene");
                final Date purchaseDate = resultSet.getDate("DataAcquisto");
                final Date nextMaintenance = resultSet.getDate("ProssimaManutenzione");
                final boolean vehicle = resultSet.getString("Automezzo").equals("Y");
                final Optional<String> toolName = Optional.ofNullable(resultSet.getString("NomeAttrezzo"));
                final Optional<String> licencePlate = Optional.ofNullable(resultSet.getString("Targa"));
                final Optional<String> typology = Optional.ofNullable(resultSet.getString("Tipologia"));
                final Optional<java.util.Date> insuranceExpiration = Optional.ofNullable(resultSet.getDate("ScadenzaAssicurazione"));

                final CapitalGood capitalGood = new CapitalGood(unitId, goodId, purchaseDate, nextMaintenance, vehicle,
                        toolName, licencePlate, typology, insuranceExpiration);
                list.add(capitalGood);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
