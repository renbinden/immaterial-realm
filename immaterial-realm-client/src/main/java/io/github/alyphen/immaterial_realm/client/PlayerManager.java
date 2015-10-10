package io.github.alyphen.immaterial_realm.client;

import io.github.alyphen.immaterial_realm.common.database.table.PlayerTable;
import io.github.alyphen.immaterial_realm.common.player.Player;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerManager {

    private ImmaterialRealmClient client;

    public PlayerManager(ImmaterialRealmClient client) {
        this.client = client;
    }

    public String getPlayerName(UUID uuid) throws SQLException {
        return client.getDatabaseManager().getDatabase().getTable(Player.class).get(uuid).getName();
    }

    public UUID getPlayerUUID(String name) throws SQLException {
        return ((PlayerTable) client.getDatabaseManager().getDatabase().getTable(Player.class)).get(name).getUUID();
    }

    public Player getPlayer(UUID uuid) throws SQLException {
        return client.getDatabaseManager().getDatabase().getTable(Player.class).get(uuid);
    }

    public Player getPlayer(String name) throws SQLException {
        return ((PlayerTable) client.getDatabaseManager().getDatabase().getTable(Player.class)).get(name);
    }

    public void addPlayer(Player player) throws SQLException {
        if (getPlayer(player.getUUID()) == null) {
            client.getDatabaseManager().getDatabase().getTable(Player.class).insert(player);
        }
    }

    public void updatePlayer(Player player) throws SQLException {
        client.getDatabaseManager().getDatabase().getTable(Player.class).update(player);
    }

}
