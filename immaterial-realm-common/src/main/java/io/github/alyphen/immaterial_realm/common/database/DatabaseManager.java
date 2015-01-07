package io.github.alyphen.immaterial_realm.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private String database;
    private Connection connection;

    public DatabaseManager(String database) {
        this.database = database;
    }

    public String getDatabase() {
        return database;
    }

    public void connect() throws SQLException {
        String url = "jdbc:sqlite:immaterial_realm_" + getDatabase() + ".db";
        connection = DriverManager.getConnection(url);
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) connect();
        return connection;
    }

}
