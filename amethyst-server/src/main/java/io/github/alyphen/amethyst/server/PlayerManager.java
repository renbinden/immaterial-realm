package io.github.alyphen.amethyst.server;

import io.github.alyphen.amethyst.common.player.Player;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.sql.*;

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
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS players (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT UNIQUE," +
                        "password_hash TEXT," +
                        "password_salt TEXT" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public String getPlayerName(long id) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
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
        Connection connection = server.getDatabaseManager().getConnection();
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
        Connection connection = server.getDatabaseManager().getConnection();
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
        Connection connection = server.getDatabaseManager().getConnection();
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

    public void addPlayer(String playerName, String password) throws SQLException {
        if (getPlayer(playerName) == null) {
            Connection connection = server.getDatabaseManager().getConnection();
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (name, password_hash, password_salt) VALUES(?, ?, ?)")) {
                statement.setString(1, playerName);
                String salt = RandomStringUtils.randomAlphanumeric(20);
                statement.setString(2, DigestUtils.sha256Hex(password + salt));
                statement.setString(3, salt);
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public boolean checkLogin(Player player, String passwordHash) throws SQLException{
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE id = ?")) {
            statement.setLong(1, player.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password_hash").equals(passwordHash);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public String getSalt(Player player) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name = ?")) {
            statement.setString(1, player.getName());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password_salt");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return generateSalt(player);
    }

    public String generateSalt(Player player) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        String salt = RandomStringUtils.randomAlphanumeric(20);
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (name) VALUES(?)")) {
            statement.setString(1, player.getName());
            statement.setString(2, salt);
            statement.executeUpdate();
            return salt;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
