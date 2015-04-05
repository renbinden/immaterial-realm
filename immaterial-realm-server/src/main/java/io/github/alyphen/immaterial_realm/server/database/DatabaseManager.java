package io.github.alyphen.immaterial_realm.server.database;

import io.github.alyphen.immaterial_realm.common.database.Database;
import io.github.alyphen.immaterial_realm.common.database.table.*;

import java.sql.SQLException;

public class DatabaseManager {

    private Database database;

    public DatabaseManager() throws SQLException {
        database = new Database("server");
        database.addTable(new ImageTable(database));
        database.addTable(new SpriteTable(database));
        database.addTable(new SpriteFrameTable(database));
        database.addTable(new CharacterTable(database));
        database.addTable(new PlayerTable(database));
    }

    public Database getDatabase() {
        return database;
    }

}
