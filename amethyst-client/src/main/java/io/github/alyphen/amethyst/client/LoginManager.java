package io.github.alyphen.amethyst.client;

import org.apache.commons.lang.RandomStringUtils;

import java.sql.*;

public class LoginManager {

    private AmethystClient client;

    public LoginManager(AmethystClient client) {
        this.client = client;
        try {
            createLoginTable();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createLoginTable() throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS login (" +
                        "id INTEGER PRIMARY KEY ASC," +
                        "name TEXT," +
                        "password_salt TEXT" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public String getSalt() throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM login WHERE name = ?")) {
            statement.setString(1, client.getPlayerName());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password_salt");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return generateSalt();
    }

    public String generateSalt() throws SQLException {
        Connection connection = client.getDatabaseManager().getConnection();
        String salt = RandomStringUtils.randomAlphanumeric(20);
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO login (name, password_salt) VALUES(?, ?)")) {
            statement.setString(1, client.getPlayerName());
            statement.setString(2, salt);
            statement.executeUpdate();
            return salt;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
