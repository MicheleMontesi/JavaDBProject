package db;

import java.util.List;
import java.util.Optional;

public interface Table<V,K> {

    String getTableName();
    boolean createTable();
    boolean dropTable();
    Optional<V> findByFiscalCode(final K fiscalCode);
    List<V> findAll();
    boolean save(final V value);
    boolean update(final V updatedValue);
    boolean delete(final K primaryKey);
}
