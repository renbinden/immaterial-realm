package io.github.alyphen.immaterial_realm.client;

import io.github.alyphen.immaterial_realm.common.player.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerManager {

    private ImmaterialRealmClient client;

    public PlayerManager(ImmaterialRealmClient client) {
        this.client = client;
        try {
            createPlayersTable();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createPlayersTable() throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS players (" +
                        "id INTEGER PRIMARY KEY," +
                        "name TEXT UNIQUE" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public String getPlayerName(long id) throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public long getPlayerId(String name) throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        }
        return -1;
    }

    public Player getPlayer(long id) throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Player getPlayer(String name) throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void addPlayer(Player player) throws SQLException {
        if (getPlayer(player.getId()) == null) {
            Connection connection = client.getDatabaseManager().getConnection();
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (id, name) VALUES(?, ?)")) {
                statement.setLong(1, player.getId());
                statement.setString(2, player.getName());
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void updatePlayer(Player player) throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE players SET name = ? WHERE id = ?")) {
            statement.setString(1, player.getName());
            statement.setLong(2, player.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
