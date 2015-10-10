package io.github.alyphen.immaterial_realm.client;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.database.Database;
import io.github.alyphen.immaterial_realm.common.database.table.*;

import java.sql.SQLException;

public class DatabaseManager {

    private Database database;

    public DatabaseManager(ImmaterialRealm immaterialRealm) throws SQLException {
        database = new Database("client");
        immaterialRealm.setDatabase(database);
        database.addTable(new ImageTable(immaterialRealm));
        database.addTable(new SpriteTable(immaterialRealm));
        database.addTable(new SpriteFrameTable(immaterialRealm));
        database.addTable(new CharacterTable(immaterialRealm));
        database.addTable(new PlayerTable(immaterialRealm));
    }

    public Database getDatabase() {
        return database;
    }

}
