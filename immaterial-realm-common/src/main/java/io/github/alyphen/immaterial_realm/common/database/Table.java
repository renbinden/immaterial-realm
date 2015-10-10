package io.github.alyphen.immaterial_realm.common.database;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;

import java.sql.SQLException;
import java.util.UUID;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

public abstract class Table<T extends TableRow> {

    private ImmaterialRealm immaterialRealm;
    private String name;
    private Class<T> type;

    public Table(ImmaterialRealm immaterialRealm, String name, Class<T> type) throws SQLException {
        this.immaterialRealm = immaterialRealm;
        this.name = name;
        this.type = type;
        create();
    }

    public Table(ImmaterialRealm immaterialRealm, Class<T> type) throws SQLException {
        this(immaterialRealm, UPPER_CAMEL.to(LOWER_UNDERSCORE, type.getSimpleName()), type);
    }

    public ImmaterialRealm getImmaterialRealm() {
        return immaterialRealm;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public abstract void create() throws SQLException;

    public abstract void insert(T object) throws SQLException;

    public abstract void update(T object) throws SQLException;

    public abstract T get(UUID uuid) throws SQLException;

}
