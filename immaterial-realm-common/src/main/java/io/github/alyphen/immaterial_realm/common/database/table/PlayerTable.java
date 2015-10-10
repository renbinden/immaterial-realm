package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.player.Player;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.util.logging.Level.SEVERE;

public class PlayerTable extends Table<Player> {

    public PlayerTable(ImmaterialRealm immaterialRealm) throws SQLException {
        super(immaterialRealm, Player.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS player (" +
                        "uuid VARCHAR(36) PRIMARY KEY," +
                        "name TEXT UNIQUE," +
                        "password_hash TEXT," +
                        "password_salt TEXT" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to create player table", exception);
        }
    }

    public void insert(Player player, String password) throws SQLException {
        if (get(player.getName()) == null) {
            Connection connection = getImmaterialRealm().getDatabase().getConnection();
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO player (uuid, name, password_hash, password_salt) VALUES(?, ?, ?, ?)"
            )) {
                statement.setString(1, player.getUUID().toString());
                statement.setString(2, player.getName());
                String salt = RandomStringUtils.randomAlphanumeric(20);
                statement.setString(3, DigestUtils.sha256Hex(password + salt));
                statement.setString(4, salt);
                if (statement.executeUpdate() == 0)
                    throw new SQLException("Failed to insert player");
            } catch (SQLException exception) {
                getImmaterialRealm().getLogger().log(SEVERE, "Failed to insert player", exception);
            }
        }
    }

    @Override
    public void insert(Player player) throws SQLException {
        insert(player, "");
    }

    @Override
    public void update(Player player) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE player SET name = ? WHERE uuid = ?")) {
            statement.setString(1, player.getName());
            statement.setString(2, player.getUUID().toString());
            statement.executeUpdate();
        }
    }

    @Override
    public Player get(UUID uuid) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"));
            }
        } catch (SQLException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to retrieve player", exception);
        }
        return null;
    }

    public Player get(String name)  throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"));
            }
        } catch (SQLException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to retrieve player", exception);
        }
        return null;
    }

    public void setPassword(Player player, String password) throws SQLException {
        if (get(player.getUUID()) == null) {
            insert(player, password);
        } else {
            Connection connection = getImmaterialRealm().getDatabase().getConnection();
            try (PreparedStatement statement = connection.prepareStatement("UPDATE player SET password_hash = ?, password_salt = ? WHERE uuid = ?")) {
                String salt = RandomStringUtils.randomAlphanumeric(20);
                statement.setString(1, DigestUtils.sha256Hex(password + salt));
                statement.setString(2, salt);
                statement.setString(3, player.getUUID().toString());
                statement.executeUpdate();
            } catch (SQLException exception) {
                getImmaterialRealm().getLogger().log(SEVERE, "Failed to update player password", exception);
            }
        }
    }

    public boolean checkLogin(Player player, String password) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE uuid = ?")) {
            statement.setString(1, player.getUUID().toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password_hash").equals(DigestUtils.sha256Hex(password + resultSet.getString("password_salt")));
            }
        } catch (SQLException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to retrieve player login details from database", exception);
        }
        return false;
    }

}
