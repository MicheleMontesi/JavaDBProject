package db.tables;

import db.Table;
import model.CertificateAcquired;
import utilities.DateConverter;

import java.sql.*;
import java.util.Date;
import java.util.*;

import static utilities.checkers.CommonCheckers.nonErasableCheck;

public class CertificateAcquiredTables implements Table<CertificateAcquired, String> {

    protected static final String ACQUISITO = "attestato_acquisito";
    private final Connection connection;

    public CertificateAcquiredTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return ACQUISITO;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE attestato_acquisito (" +
                            "   CodiceFiscale char(16) NOT NULL," +
                            "   DataAcquisizione date NOT NULL," +
                            "   Nome char(30) NOT NULL," +
                            "   PRIMARY KEY (CodiceFiscale,DataAcquisizione)," +
                            "   UNIQUE KEY ID_ATTESTATO_ACQUISITO_IND (CodiceFiscale,DataAcquisizione)," +
                            "   KEY FKACQUISIZIONE_IND (Nome)," +
                            "   CONSTRAINT FKACQUISIZIONE_FK FOREIGN KEY (Nome) REFERENCES tipologia_attestato (Nome)," +
                            "   CONSTRAINT FKCONSEGUIRE FOREIGN KEY (CodiceFiscale) REFERENCES dipendente (CodiceFiscale)" +
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
            statement.executeQuery("DROP TABLE " + ACQUISITO);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<CertificateAcquired>> findByCode(String code) {
        final String query = "SELECT * FROM " + ACQUISITO +
                " WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CertificateAcquired> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + ACQUISITO);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(CertificateAcquired certificateAcquired) {
        final String query = "INSERT INTO " + ACQUISITO + "(codicefiscale, dataacquisizione, nome) " +
                "VALUES (?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, certificateAcquired.fiscalCode());
            statement.setDate(2, DateConverter.dateToSqlDate(certificateAcquired.acquisitionDate()));
            statement.setString(3, certificateAcquired.certificateName());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(CertificateAcquired certificateAcquired) {
        return false;
    }

    @Override
    public void delete(String code) {

    }

    public void deleteByParameters(String fiscalCode, Date acquisitionDate, String name) {
        final String query = "DELETE FROM " + ACQUISITO + " WHERE CodiceFiscale = ? " +
                "AND DataAcquisizione = ? AND Nome = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, fiscalCode);
            statement.setDate(2, DateConverter.dateToSqlDate(acquisitionDate));
            statement.setString(3, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            nonErasableCheck();
        }
    }

    @Override
    public List<CertificateAcquired> readFromResultSet(final ResultSet resultSet) {
        final List<CertificateAcquired> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String workerFiscalCode = resultSet.getString("CodiceFiscale");
                final Date acquisitionDate = resultSet.getDate("DataAcquisizione");
                final String beginTime = resultSet.getString("Nome");

                final CertificateAcquired certificateAcquired = new CertificateAcquired(workerFiscalCode,
                        acquisitionDate, beginTime);
                list.add(certificateAcquired);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
