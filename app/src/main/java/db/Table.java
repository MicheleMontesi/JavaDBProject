package db;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface Table<V,K> {

    String getTableName();
    boolean createTable();
    boolean dropTable();
    Optional<List<V>> findByCode(final K code);
    List<V> findAll();
    void save(final V value);
    boolean update(final V updatedValue);
    void delete(final K primaryKey);
    List<V> readFromResultSet(final ResultSet resultSet);
}
