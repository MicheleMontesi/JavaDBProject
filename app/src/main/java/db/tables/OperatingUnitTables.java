package db.tables;

import db.Table;
import model.OperatingUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static utilities.checkers.CommonCheckers.nonErasableCheck;

public class OperatingUnitTables implements Table<OperatingUnit, String> {

    protected static final String UNITA_OPERATIVA = "unita_operativa";
    private final Connection connection;

    public OperatingUnitTables(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return UNITA_OPERATIVA;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE " + UNITA_OPERATIVA + " (" +
                            "CodiceUnita CHAR(5) NOT NULL, " +
                            "Tipologia CHAR(40) NOT NULL, " +
                            "Denominazione CHAR(20) NOT NULL, " +
                            "Ubicazione CHAR(50) NOT NULL, " +
                            "PostiLetto INT NOT NULL, " +
                            "NumeroPazienti INT NOT NULL, " +
                            "AutorizzazioneAlFunzionamento CHAR(1) NOT NULL, " +
                            "Accreditamento CHAR(1) NOT NULL, " +
                            "PRIMARY KEY (CodiceUnita), " +
                            "UNIQUE KEY ID_UNITA_OPERATIVA_IND (CodiceUnita)" +
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
            statement.executeUpdate("DROP TABLE " + UNITA_OPERATIVA);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<OperatingUnit>> findByCode(String code) {
        final String query = "SELECT * FROM " + UNITA_OPERATIVA + " WHERE CodiceUnita = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public List<OperatingUnit> findFull() {
        final String query = "SELECT * " +
                "FROM " + UNITA_OPERATIVA +
                " WHERE PostiLetto = NumeroPazienti";
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(query);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OperatingUnit> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + UNITA_OPERATIVA);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(OperatingUnit opUnit) {
        final String query = "INSERT INTO " + UNITA_OPERATIVA + "(CodiceUnita, Tipologia, Denominazione, Ubicazione, " +
                "PostiLetto, NumeroPazienti, AutorizzazioneAlFunzionamento, Accreditamento) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, opUnit.unitId());
            statement.setString(2, opUnit.type());
            statement.setString(3, opUnit.name());
            statement.setString(4, opUnit.location());
            statement.setInt(5, opUnit.bedsNumber());
            statement.setInt(6, opUnit.patientsNumber());
            statement.setString(7, opUnit.operatingAuth() ? "Y" : "N");
            statement.setString(8, opUnit.accreditation() ? "Y" : "N");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(OperatingUnit opUnit) {
        final String query = "UPDATE " + UNITA_OPERATIVA + " SET " +
                "Tipologia = ?, " +
                "Denominazione = ?, " +
                "Ubicazione = ?, " +
                "PostiLetto = ?, " +
                "NumeroPazienti = ?, " +
                "AutorizzazioneAlFunzionamento = ?, " +
                "Accreditamento = ? " +
                "WHERE CodiceUnita = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, opUnit.type());
            statement.setString(2, opUnit.name());
            statement.setString(3, opUnit.location());
            statement.setInt(4, opUnit.bedsNumber());
            statement.setInt(5, opUnit.patientsNumber());
            statement.setString(6, opUnit.operatingAuth() ? "Y" : "N");
            statement.setString(7, opUnit.accreditation() ? "Y" : "N");
            statement.setString(8, opUnit.unitId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String unitId) {
        final String query = "DELETE FROM " + UNITA_OPERATIVA + " WHERE CodiceUnita = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setString(1, unitId);
            statement.executeUpdate();
        } catch (SQLException e) {
            nonErasableCheck();
        }
    }

    @Override
    public List<OperatingUnit> readFromResultSet(final ResultSet resultSet) {
        final List<OperatingUnit> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String unitId = resultSet.getString("CodiceUnita");
                final String type = resultSet.getString("Tipologia");
                final String name = resultSet.getString("Denominazione");
                final String location = resultSet.getString("Ubicazione");
                final int bedsNumber = resultSet.getInt("PostiLetto");
                final int patientsNumber = resultSet.getInt("NumeroPazienti");
                final boolean operatingAuth = resultSet.getString("AutorizzazioneAlFunzionamento").equals("Y");
                final boolean accreditation = resultSet.getString("Accreditamento").equals("Y");

                final OperatingUnit opUnit = new OperatingUnit(unitId, type, name, location, bedsNumber,
                        patientsNumber, operatingAuth, accreditation);
                list.add(opUnit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
