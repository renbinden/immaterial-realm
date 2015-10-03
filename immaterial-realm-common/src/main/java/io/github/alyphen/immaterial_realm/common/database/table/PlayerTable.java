package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.database.Database;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.player.Player;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.logging.Level.SEVERE;

public class PlayerTable extends Table<Player> {

    public PlayerTable(Database database) throws SQLException {
        super(database, Player.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS player (" +
                        "id INTEGER PRIMARY KEY," +
                        "name TEXT UNIQUE," +
                        "password_hash TEXT," +
                        "password_salt TEXT" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to create player table", exception);
        }
    }

    public long insert(Player player, String password) throws SQLException {
        if (get(player.getName()) == null) {
            Connection connection = getDatabase().getConnection();
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO player (name, password_hash, password_salt) VALUES(?, ?, ?)",
                    RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, player.getName());
                String salt = RandomStringUtils.randomAlphanumeric(20);
                statement.setString(2, DigestUtils.sha256Hex(password + salt));
                statement.setString(3, salt);
                if (statement.executeUpdate() == 0) throw new SQLException("Failed to insert player");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        player.setId(id);
                        return id;
                    }
                }
            } catch (SQLException exception) {
                ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to insert player", exception);
            }
            throw new SQLException("Failed to insert player");
        }
        throw new SQLException("Failed to insert player");
    }

    @Override
    public long insert(Player player) throws SQLException {
        return insert(player, "");
    }

    @Override
    public long update(Player player) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE player SET name = ? WHERE id = ?")) {
            statement.setString(1, player.getName());
            statement.setLong(2, player.getId());
            statement.executeUpdate();
            return player.getId();
        }
    }

    @Override
    public Player get(long id) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to retrieve player", exception);
        }
        return null;
    }

    public Player get(String name)  throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to retrieve player", exception);
        }
        return null;
    }

    public void setPassword(Player player, String password) throws SQLException {
        if (get(player.getId()) == null) {
            insert(player, password);
        } else {
            Connection connection = getDatabase().getConnection();
            try (PreparedStatement statement = connection.prepareStatement("UPDATE player SET password_hash = ?, password_salt = ? WHERE id = ?")) {
                String salt = RandomStringUtils.randomAlphanumeric(20);
                statement.setString(1, DigestUtils.sha256Hex(password + salt));
                statement.setString(2, salt);
                statement.setLong(3, player.getId());
                statement.executeUpdate();
            } catch (SQLException exception) {
                ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to update player password", exception);
            }
        }
    }

    public boolean checkLogin(Player player, String password) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE id = ?")) {
            statement.setLong(1, player.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password_hash").equals(DigestUtils.sha256Hex(password + resultSet.getString("password_salt")));
            }
        } catch (SQLException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to retrieve player login details from database", exception);
        }
        return false;
    }

}
