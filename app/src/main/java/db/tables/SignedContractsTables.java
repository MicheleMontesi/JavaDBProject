package db.tables;

import db.Table;
import model.SignedContract;
import utilities.DateConverter;

import java.sql.*;
import java.util.Date;
import java.util.*;

public class SignedContractsTables implements Table<SignedContract, String> {

    protected static final String STIPULATO = "contratto_stipulato";
    private final Connection connection;

    public SignedContractsTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return STIPULATO;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE contratto_stipulato (" +
                            "  CodiceFiscale char(16) NOT NULL," +
                            "  DataStipulazione date NOT NULL," +
                            "  DataFine date NOT NULL," +
                            "  Nome char(20) NOT NULL," +
                            "  PRIMARY KEY (CodiceFiscale,DataStipulazione)," +
                            "  UNIQUE KEY ID_CONTRATTO_STIPULATO_IND (CodiceFiscale,DataStipulazione)," +
                            "  KEY FKSTIPULARE_IND (Nome)," +
                            "  CONSTRAINT FKFIRMARE FOREIGN KEY (CodiceFiscale) REFERENCES dipendente (CodiceFiscale)," +
                            "  CONSTRAINT FKSTIPULARE_FK FOREIGN KEY (Nome) REFERENCES tipologia_contratto (Nome)" +
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
            statement.executeQuery("DROP TABLE " + STIPULATO);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<SignedContract>> findByCode(String code) {
        return Optional.empty();
    }

    public Optional<List<SignedContract>> findByParameters(String code, String name) {
        final String query = "SELECT * FROM " +
                STIPULATO + " WHERE CodiceFiscale = ? AND Nome = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            statement.setString(2, name);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SignedContract> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + STIPULATO);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(SignedContract signedContract) {
        final String query = "INSERT INTO " + STIPULATO + "(codicefiscale, datastipulazione, datafine, nome) " +
                "VALUES (?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, signedContract.fiscalCode());
            statement.setDate(2, DateConverter.dateToSqlDate(signedContract.stipulationDate()));
            statement.setDate(3, DateConverter.dateToSqlDate(signedContract.endDate()));
            statement.setString(4, signedContract.contractName());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(SignedContract signedContract) {
        final String query = "UPDATE " + STIPULATO + " SET " +
                "DataFine = ? " +
                "WHERE CodiceFiscale = ? AND DataStipulazione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, DateConverter.dateToSqlDate(signedContract.endDate()));
            statement.setString(2, signedContract.fiscalCode());
            statement.setDate(3, DateConverter.dateToSqlDate(signedContract.stipulationDate()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String primaryKey) {

    }

    public void deleteByParameters(String fiscalCode, Date stipulationDate) {
        final String query = "DELETE FROM " + STIPULATO +
                " WHERE CodiceFiscale = ? AND DataStipulazione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, fiscalCode);
            statement.setDate(2, DateConverter.dateToSqlDate(stipulationDate));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<SignedContract> readFromResultSet(ResultSet resultSet) {
        final List<SignedContract> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String workerFiscalCode = resultSet.getString("CodiceFiscale");
                final Date stipulationDate = DateConverter.sqlDateToDate(resultSet.getDate("DataStipulazione"));
                final Date endDate = DateConverter.sqlDateToDate(resultSet.getDate("OraInizio"));
                final String name = resultSet.getString("Nome");

                final SignedContract signedContract = new SignedContract(workerFiscalCode, stipulationDate, endDate, name);
                list.add(signedContract);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
