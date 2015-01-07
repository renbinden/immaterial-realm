package io.github.alyphen.immaterial_realm.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginManager {

    private ImmaterialRealmClient client;

    public LoginManager(ImmaterialRealmClient client) {
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
