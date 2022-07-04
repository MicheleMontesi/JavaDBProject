package db.tables;

import db.Table;
import model.Hosting;
import utilities.DateConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HostingTables implements Table<Hosting, String> {

    protected static final String OSPITAZIONE = "ospitazione";
    private final Connection connection;

    public HostingTables(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return  OSPITAZIONE;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE ospitazione (" +
                            "   CodiceFiscale char(16) NOT NULL," +
                            "   DataInizio date NOT NULL," +
                            "   DataFine date DEFAULT NULL," +
                            "   CodiceUnita char(5) NOT NULL," +
                            "   PRIMARY KEY (CodiceFiscale,DataInizio)," +
                            "   UNIQUE KEY ID_OSPITAZIONE_IND (CodiceFiscale,DataInizio)," +
                            "   KEY FKFORNIRE_IND (CodiceUnita)," +
                            "   CONSTRAINT FKFORNIRE_FK FOREIGN KEY (CodiceUnita) REFERENCES unita_operativa (CodiceUnita)," +
                            "   CONSTRAINT FKOSPITARE FOREIGN KEY (CodiceFiscale) REFERENCES paziente (CodiceFiscale)" +
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
            statement.executeQuery("DROP TABLE " + OSPITAZIONE);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<Hosting>> findByCode(String code) {
        return Optional.empty();
    }

    public Optional<List<Hosting>> findByParameters(String code, java.util.Date signedDate, String name) {
        final String query = "SELECT * FROM " +
                OSPITAZIONE + " WHERE CodiceFiscale = ? AND DataInizio = ? AND CodiceUnita = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, code);
            statement.setDate(2, DateConverter.dateToSqlDate(signedDate));
            statement.setString(3, name);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Hosting> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + OSPITAZIONE);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Hosting hosting) {
        final String query = "INSERT INTO " + OSPITAZIONE + "(codicefiscale, datainizio, datafine, codiceunita) " +
                "VALUES (?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, hosting.fiscalCode());
            statement.setDate(2, DateConverter.dateToSqlDate(hosting.beginDate()));
            statement.setDate(3, hosting.endDate().map(DateConverter::dateToSqlDate).orElse(null));
            statement.setString(4, hosting.unitId());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(Hosting hosting) {
        final String query = "UPDATE " + OSPITAZIONE + " SET " +
                "DataFine = ? " +
                "WHERE CodiceFiscale = ? AND DataInizio = ? AND CodiceUnita = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, hosting.endDate().map(DateConverter::dateToSqlDate).orElse(null));
            statement.setString(2, hosting.fiscalCode());
            statement.setDate(3, DateConverter.dateToSqlDate(hosting.beginDate()));
            statement.setString(4, hosting.unitId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String primaryKey) {

    }

    public void deleteByParameters(String CodiceFiscale, java.util.Date beginDate, String unitId) {
        final String query = "DELETE FROM " + OSPITAZIONE + " WHERE CodiceFiscale = ? " +
                "AND DataInizio = ? AND CodiceUnita = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setString(1, CodiceFiscale);
            statement.setDate(2, DateConverter.dateToSqlDate(beginDate));
            statement.setString(3, unitId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    @Override
    public List<Hosting> readFromResultSet(final ResultSet resultSet) {
        final List<Hosting> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String workerFiscalCode = resultSet.getString("CodiceFiscale");
                final java.util.Date beginDate = DateConverter.sqlDateToDate(resultSet.getDate("DataInizio"));
                final Optional<java.util.Date> endDate = Optional.ofNullable(DateConverter.sqlDateToDate(
                        resultSet.getDate("DataFine")));
                final String unitId = resultSet.getString("CodiceUnita");

                final Hosting hosting = new Hosting(workerFiscalCode, beginDate, endDate, unitId);
                list.add(hosting);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
