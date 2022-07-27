package db.tables;

import db.Table;
import model.TherapyDrug;
import utilities.DateConverter;

import java.sql.*;
import java.util.Date;
import java.util.*;

import static utilities.checkers.CommonCheckers.nonErasableCheck;

public class TherapyDrugsTable implements Table<TherapyDrug, String> {

    protected static final String FARMACO_TERAPIA = "farmaco_terapia";
    private final Connection connection;

    public TherapyDrugsTable(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return FARMACO_TERAPIA;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE farmaco_terapia (" +
                            "  CodiceTerapia int NOT NULL," +
                            "  CodiceConsumazione int NOT NULL," +
                            "  DataAssunzione date NOT NULL," +
                            "  Quantita int NOT NULL," +
                            "  CodiceFiscale char(16) NOT NULL," +
                            "  CodFarmaco int NOT NULL," +
                            "  PRIMARY KEY (CodiceTerapia,CodiceConsumazione)," +
                            "  UNIQUE KEY ID_FARMACO_TERAPIA_IND (CodiceTerapia,CodiceConsumazione)," +
                            "  KEY FKSOMMINISTRA_IND (CodiceFiscale)," +
                            "  KEY FKCONSUMARE_IND (CodFarmaco)," +
                            "  CONSTRAINT FKCONSUMARE_FK FOREIGN KEY (CodFarmaco) REFERENCES farmaco (CodFarmaco)," +
                            "  CONSTRAINT FKDEFINIRE FOREIGN KEY (CodiceTerapia) REFERENCES terapia (CodiceTerapia)," +
                            "  CONSTRAINT FKSOMMINISTRA_FK FOREIGN KEY (CodiceFiscale) REFERENCES dipendente (CodiceFiscale)" +
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
            statement.executeQuery("DROP TABLE " + FARMACO_TERAPIA);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<List<TherapyDrug>> findByCode(String code) {
        return Optional.empty();
    }

    public Optional<List<TherapyDrug>> findByParameters(int therapyId, int consumptionId) {
        final String query = "SELECT * FROM " +
                FARMACO_TERAPIA + " WHERE CodiceTerapia = ? AND CodiceConsumazione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, therapyId);
            statement.setInt(2, consumptionId);
            final ResultSet resultSet = statement.executeQuery();
            return Optional.of(readFromResultSet(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TherapyDrug> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + FARMACO_TERAPIA);
            return readFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public void save(TherapyDrug therapyDrug) {
        final String query = "INSERT INTO " + FARMACO_TERAPIA + "(codiceterapia, codiceconsumazione, dataassunzione, " +
                "quantita, codicefiscale, codfarmaco) " +
                "VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, therapyDrug.therapyId());
            statement.setInt(2, therapyDrug.consumptionId());
            statement.setDate(3, DateConverter.dateToSqlDate(therapyDrug.consumptionDate()));
            statement.setInt(4, therapyDrug.quantity());
            statement.setString(5, therapyDrug.administratorFiscalCode());
            statement.setInt(6, therapyDrug.drugId());
            statement.executeUpdate();
        }catch (final SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(TherapyDrug therapyDrug) {
        final String query = "UPDATE " + FARMACO_TERAPIA + " SET " +
                "DataAssunzione = ?, " +
                "Quantita = ?, " +
                "CodiceFiscale = ?, " +
                "CodFarmaco = ? " +
                "WHERE CodiceTerapia = ? AND CodiceConsumazione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, DateConverter.dateToSqlDate(therapyDrug.consumptionDate()));
            statement.setInt(2, therapyDrug.quantity());
            statement.setString(3, therapyDrug.administratorFiscalCode());
            statement.setInt(4, therapyDrug.drugId());
            statement.setInt(5, therapyDrug.therapyId());
            statement.setInt(6, therapyDrug.consumptionId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String primaryKey) {

    }

    public void deleteByParameters(int therapyId, int consumptionId) {
        final String query = "DELETE FROM " + FARMACO_TERAPIA +
                " WHERE CodiceTerapia = ? AND CodiceConsumazione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, therapyId);
            statement.setInt(2, consumptionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            nonErasableCheck();
        }
    }

    @Override
    public List<TherapyDrug> readFromResultSet(ResultSet resultSet) {
        final List<TherapyDrug> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int therapyId = resultSet.getInt("CodiceTerapia");
                final int consumptionId = resultSet.getInt("CodiceConsumazione");
                final Date consumptionDate = resultSet.getDate("DataAssunzione");
                final int quantity = resultSet.getInt("Quantita");
                final String fiscalCode = resultSet.getString("CodiceFiscale");
                final int drugId = resultSet.getInt("CodFarmaco");

                final TherapyDrug therapyDrug = new TherapyDrug(therapyId, consumptionId, consumptionDate, quantity,
                        fiscalCode, drugId);
                list.add(therapyDrug);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
