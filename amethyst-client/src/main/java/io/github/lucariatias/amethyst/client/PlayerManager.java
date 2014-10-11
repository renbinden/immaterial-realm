package io.github.lucariatias.amethyst.client;

import org.apache.commons.lang.RandomStringUtils;

import java.sql.*;

public class PlayerManager {

    private AmethystClient client;

    public PlayerManager(AmethystClient client) {
        this.client = client;
        try {
            createPlayersTable();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createPlayersTable() throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS players (" +
                            "id INTEGER PRIMARY KEY ASC," +
                            "name TEXT," +
                            "password_salt TEXT" +
                    ")"
            );
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public String getSalt() throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players WHERE name = \"" + client.getPlayerName() + "\"");
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return generateSalt();
    }

    public String generateSalt() throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        String salt = RandomStringUtils.randomAlphanumeric(20);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO players (name, password_salt) VALUES(\"" + client.getPlayerName() + "\", \"" + salt + "\")");
            return salt;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
