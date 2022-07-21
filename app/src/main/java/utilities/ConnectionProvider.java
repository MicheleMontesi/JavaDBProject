package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private final String username;
    private final String password;
    private final String dbName;

    public ConnectionProvider(final String username, final String password, final String dbName) {
        this.username = username;
        this.password = password;
        this.dbName = dbName;
    }

    public Connection getMySQLConnection() {
        final String dbUri = "jdbc:mysql://localhost:3306/" + this.dbName;
        try {
            return DriverManager.getConnection(dbUri, this.username, this.password);
        } catch (SQLException e) {
            throw new RuntimeException("Could not establish a connection with db", e);
        }
    }
}
