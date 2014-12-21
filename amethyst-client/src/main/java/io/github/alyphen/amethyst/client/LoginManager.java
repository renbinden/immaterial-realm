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
                        "name TEXT" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
