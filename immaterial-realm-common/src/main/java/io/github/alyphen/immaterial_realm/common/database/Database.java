package io.github.alyphen.immaterial_realm.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

public class Database {

    private String name;
    private Connection connection;
    private Map<String, Table<?>> tables;

    public Database(String name) throws SQLException {
        this.name = name;
        tables = new HashMap<>();
        connect();
    }

    public String getName() {
        return name;
    }

    public void connect() throws SQLException {
        if (connection != null) return;
        String url = "jdbc:sqlite:immaterial_realm_" + getName() + ".db";
        connection = DriverManager.getConnection(url);
    }

    public Connection getConnection() {
        return connection;
    }

    public void addTable(Table<?> table) {
        tables.put(table.getName(), table);
    }

    public Table<?> getTable(String name) {
        return tables.get(name);
    }

    public <T extends TableRow> Table<T> getTable(Class<T> type) throws SQLException {
        Table<?> table = tables.get(UPPER_CAMEL.to(LOWER_UNDERSCORE, type.getSimpleName()));
        if (table != null)
            return (Table<T>) table;
        else
            return null;
    }

}
