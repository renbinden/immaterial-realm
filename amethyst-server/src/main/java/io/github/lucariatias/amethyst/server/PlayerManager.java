package io.github.lucariatias.amethyst.server;

import io.github.lucariatias.amethyst.common.player.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerManager {

    private AmethystServer server;

    public PlayerManager(AmethystServer server) {
        this.server = server;
        try {
            createPlayersTable();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createPlayersTable() throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS players (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT," +
                            "password_hash TEXT" +
                    ")"
            );
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public String getPlayerName(long id) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players WHERE id = " + id);
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public long getPlayerId(String name) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players WHERE name = \"" + name + "\"");
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        }
        return -1;
    }

    public Player getPlayer(long id) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players WHERE id = " + id);
            if (resultSet.next()) {
                return new Player(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Player getPlayer(String name) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players WHERE name = \"" + name + "\"");
            if (resultSet.next()) {
                return new Player(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void addPlayer(String playerName, String passwordHash) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO players (name, password_hash) VALUES(\"" + playerName + "\", \"" + passwordHash + "\")");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean checkLogin(Player player, String passwordHash) throws SQLException{
        Connection connection = server.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players WHERE id = " + player.getId());
            if (resultSet.next()) {
                return resultSet.getString("password_hash").equals(passwordHash);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

}
