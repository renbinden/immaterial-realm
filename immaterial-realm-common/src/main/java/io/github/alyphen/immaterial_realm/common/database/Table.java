package io.github.alyphen.immaterial_realm.common.database;

import java.sql.SQLException;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

public abstract class Table<T extends TableRow> {

    private Database database;
    private String name;
    private Class<T> type;

    public Table(Database database, String name, Class<T> type) throws SQLException {
        this.database = database;
        this.name = name;
        this.type = type;
        create();
    }

    public Table(Database database, Class<T> type) throws SQLException {
        this(database, UPPER_CAMEL.to(LOWER_UNDERSCORE, type.getSimpleName()), type);
    }

    public Database getDatabase() {
        return database;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public abstract void create() throws SQLException;

    public abstract long insert(T object) throws SQLException;

    public abstract long update(T object) throws SQLException;

    public abstract T get(long id) throws SQLException;

}
