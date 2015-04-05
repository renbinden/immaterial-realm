package io.github.alyphen.immaterial_realm.client;

import io.github.alyphen.immaterial_realm.common.database.table.PlayerTable;
import io.github.alyphen.immaterial_realm.common.player.Player;

import java.sql.SQLException;

public class PlayerManager {

    private ImmaterialRealmClient client;

    public PlayerManager(ImmaterialRealmClient client) {
        this.client = client;
    }

    public String getPlayerName(long id) throws SQLException {
        return client.getDatabaseManager().getDatabase().getTable(Player.class).get(id).getName();
    }

    public long getPlayerId(String name) throws SQLException {
        return ((PlayerTable) client.getDatabaseManager().getDatabase().getTable(Player.class)).get(name).getId();
    }

    public Player getPlayer(long id) throws SQLException {
        return client.getDatabaseManager().getDatabase().getTable(Player.class).get(id);
    }

    public Player getPlayer(String name) throws SQLException {
        return ((PlayerTable) client.getDatabaseManager().getDatabase().getTable(Player.class)).get(name);
    }

    public void addPlayer(Player player) throws SQLException {
        if (getPlayer(player.getId()) == null) {
            client.getDatabaseManager().getDatabase().getTable(Player.class).insert(player);
        }
    }

    public void updatePlayer(Player player) throws SQLException {
        client.getDatabaseManager().getDatabase().getTable(Player.class).update(player);
    }

}
